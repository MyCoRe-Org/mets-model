package org.mycore.mets.misc;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.mycore.mets.model.struct.LogicalDiv;
import org.mycore.mets.model.struct.LogicalStructMap;
import org.mycore.mets.model.struct.PhysicalDiv;
import org.mycore.mets.model.struct.PhysicalStructMap;
import org.mycore.mets.model.struct.PhysicalSubDiv;
import org.mycore.mets.model.struct.SmLink;
import org.mycore.mets.model.struct.StructLink;

/**
 * <p>Generates the struct link section for a mets object. You can link the logical with the physical section by
 * implementing the {@link #getLinkedMap} method.</p>
 *
 * <p>Its not required to link every single logical or physical div. This class can interpolate missing both logical
 * or physical divs if "fail easy" is disabled.</p>
 *
 * <h2>logical div interpolation:</h2>
 * <ol>
 *     <li>add a physical div to each logical div missing</li>
 *     <li>if the root logical div is missing, its linked with the first physical div</li>
 *     <li>all other logical divs are linked with the first phyiscal div of an ancestor</li>
 *     <li>if the logical div does not have any ancestors or the ancestors are not linked with physical divs either,
 *     then the preceding logical divs are searched for a physical div</li>
 * </ol>
 *
 * <h2>physical div interpolation:</h2>
 * <ol>
 *     <li>each missing physical div has to be linked with a logical div</li>
 *     <li>if the first physical div is not linked, then its added to the root logical div</li>
 *     <li>Take the preceding phyisical div (it will be linked due to that the missing phyiscal divs are handled in
 *     order). Check to which logical divs (can be multiple) this preceding physical div is linked. Take the last
 *     logical div of that list and link the missing physical div to it.</li>
 * </ol>
 *
 * @author Matthias Eichner
 */
public abstract class StructLinkGenerator {

    /**
     * If logical div's should be added if they are missing. Missing means that {@link #getLinkedMap} does not contain
     * a logical div which is part of the logical struct map. True by default.
     */
    private boolean interpolateLogical;

    /**
     * If physical div's should be added if they are missing. Missing means that {@link #getLinkedMap} does not contain
     * a physical div which is part of the physical struct map. True by default.
     */
    private boolean interpolatePhysical;

    /**
     * If interpolation for either logical or physical is disabled and one of the required mets:div elements is missing
     * an {@link StructLinkGenerationException} will be thrown.
     */
    private boolean failEasy;

    /**
     * Creates a new StructLinkGenerator with interpolation enabled and fail-easy disabled.
     */
    public StructLinkGenerator() {
        this.interpolateLogical = true;
        this.interpolatePhysical = true;
        this.failEasy = false;
    }

    /**
     * Sets whether the generator should throw an exception on missing links.
     *
     * @param failEasy true to throw an exception when links are missing
     */
    public void setFailEasy(boolean failEasy) {
        this.failEasy = failEasy;
    }

    /**
     * Sets whether logical divs without physical links should be interpolated.
     *
     * @param interpolateLogical true to enable logical interpolation
     */
    public void setInterpolateLogical(boolean interpolateLogical) {
        this.interpolateLogical = interpolateLogical;
    }

    /**
     * Sets whether physical divs without logical links should be interpolated.
     *
     * @param interpolatePhysical true to enable physical interpolation
     */
    public void setInterpolatePhysical(boolean interpolatePhysical) {
        this.interpolatePhysical = interpolatePhysical;
    }

    /**
     * Returns the links between the logical and the physical struct map. Each logical div can be linked with one or
     * multiple physical divs.
     *
     * @param physicalStructMap the physical part of the mets
     * @param logicalStructMap the logical part part of the mets
     * @return map between logical and physical divs
     */
    protected abstract Map<LogicalDiv, List<PhysicalSubDiv>> getLinkedMap(PhysicalStructMap physicalStructMap,
        LogicalStructMap logicalStructMap);

    /**
     * Creates the struct link section of the given {@link PhysicalStructMap} and {@link LogicalStructMap}object.
     *
     * @param physicalStructMap the physical part of the mets
     * @param logicalStructMap the logical part part of the mets
     * @return the new generated struct link
     */
    public StructLink generate(PhysicalStructMap physicalStructMap, LogicalStructMap logicalStructMap) {
        List<PhysicalSubDiv> physicalDivs = physicalStructMap.getDivContainer().getChildren();
        if (physicalDivs.isEmpty()) {
            throw new StructLinkGenerationException("Unable to create struct link section because there "
                + "are no physical <mets:div> elements.");
        }

        Map<LogicalDiv, List<PhysicalSubDiv>> linkedMap = getLinkedMap(physicalStructMap, logicalStructMap);
        LogicalLink rootLink = LogicalLink.of(logicalStructMap, linkedMap);

        handleLogicalStructMap(rootLink, physicalStructMap, logicalStructMap);
        handlePhysicalStructMap(rootLink, physicalStructMap, logicalStructMap);

        return buildStructLink(rootLink);
    }

    /**
     * Handles logical divs that are not yet linked to physical divs by interpolating missing links.
     *
     * @param rootLink          the root logical link
     * @param physicalStructMap the physical structure map
     * @param logicalStructMap  the logical structure map
     */
    protected void handleLogicalStructMap(LogicalLink rootLink, PhysicalStructMap physicalStructMap,
        LogicalStructMap logicalStructMap) {
        // list of empty links
        List<LogicalLink> emptyLinks = rootLink.emptyLinks();

        // nothing missing -> all fine, nothing to handle
        if (emptyLinks.isEmpty()) {
            return;
        }
        // some divs are missing, we are not interpolating and we fail easy -> exception
        if (!interpolateLogical && failEasy) {
            throw new StructLinkGenerationException(
                "some logical ids are not set: " + emptyLinks.stream().map(LogicalLink::getLogicalDiv).toList());
        }
        // interpolate logical divs
        interpolateLogicalDivs(emptyLinks, physicalStructMap, logicalStructMap);
    }

    /**
     * For each logical div which is not linked with a physical div (content of the emptyLinks list) this method tries
     * to get the best physical div fit. At the end of this method there shouldn't be any link without a physical div.
     * 
     * @param emptyLinks the links to assign a physical div
     * @param physicalStructMap the physical struct map
     * @param logicalStructMap the logical struct map
     */
    protected void interpolateLogicalDivs(List<LogicalLink> emptyLinks, PhysicalStructMap physicalStructMap,
        LogicalStructMap logicalStructMap) {
        PhysicalDiv divContainer = physicalStructMap.getDivContainer();
        PhysicalSubDiv firstPhysicalDiv = divContainer.getChildren().getFirst();
        emptyLinks.forEach(emptyLink -> {
            if (emptyLink.getLogicalDiv().equals(logicalStructMap.getDivContainer())) {
                // root node -> always linked with the first physical div
                addPhysicalDivToLink(emptyLink, firstPhysicalDiv, divContainer);
            } else {
                // all other nodes link with children first and then with the last physical div of the
                // preceding siblings and descendants
                PhysicalSubDiv physicalDiv = emptyLink.findFirstPhysicalDivOfAncestors()
                    .orElse(emptyLink.findLastPhysicalDivOfDescendantsAndPrecedingSiblings().orElse(firstPhysicalDiv));
                addPhysicalDivToLink(emptyLink, physicalDiv, divContainer);
            }
        });
    }

    /**
     * Handles physical divs that are not yet linked to logical divs by interpolating missing links.
     *
     * @param rootLink          the root logical link
     * @param physicalStructMap the physical structure map
     * @param logicalStructMap  the logical structure map
     */
    protected void handlePhysicalStructMap(LogicalLink rootLink, PhysicalStructMap physicalStructMap,
        LogicalStructMap logicalStructMap) {
        Set<PhysicalSubDiv> linkedPhysicalDivs = rootLink.getAllPhysicalDivs();
        List<PhysicalSubDiv> physicalDivs = physicalStructMap.getDivContainer().getChildren();
        List<PhysicalSubDiv> missingPhysicalDivs = new ArrayList<>(physicalDivs);
        missingPhysicalDivs.removeAll(linkedPhysicalDivs);

        // nothing missing -> all fine, nothing to handle
        if (missingPhysicalDivs.isEmpty()) {
            return;
        }
        // some divs are missing, we are not interpolating and we fail easy -> exception
        if (!interpolatePhysical && failEasy) {
            throw new StructLinkGenerationException(
                "some physical ids are not set: " + missingPhysicalDivs);
        }
        // interpolate physical divs
        interpolatePhysicalDivs(missingPhysicalDivs, rootLink, physicalStructMap, logicalStructMap);
    }

    /**
     * Tries to interpolate missing physical divs by assigning each of them to the logical div that
     * most recently owns the preceding physical div.
     * <p>
     * The physical divs are processed in physical order. For each missing physical div, the
     * preceding physical div is determined from the physical struct map. The missing physical div is
     * then attached to the last logical link that still contains this preceding physical div. This
     * causes gaps between explicitly linked physical divs to be filled with the logical section that
     * is currently active at that position.
     * <p>
     * If the first physical div is missing, it is linked to the root logical link.
     *
     * @param missingPhysicalDivs ordered list of physical divs that are currently not linked
     * @param rootLink the root logical link
     * @param physicalStructMap the physical struct map
     * @param logicalStructMap the logical struct map
     */
    protected void interpolatePhysicalDivs(List<PhysicalSubDiv> missingPhysicalDivs, LogicalLink rootLink,
        PhysicalStructMap physicalStructMap, LogicalStructMap logicalStructMap) {
        PhysicalDiv divContainer = physicalStructMap.getDivContainer();
        List<PhysicalSubDiv> physicalDivs = divContainer.getChildren();
        for (PhysicalSubDiv missingPhysicalDiv : missingPhysicalDivs) {
            int index = physicalDivs.indexOf(missingPhysicalDiv);
            if (index == 0) {
                addPhysicalDivToLink(rootLink, missingPhysicalDiv, divContainer);
                continue;
            }
            PhysicalSubDiv precedingPhysicalDiv = physicalDivs.get(index - 1);
            LogicalLink lastMatch = getLogicalLink(rootLink, missingPhysicalDiv, precedingPhysicalDiv);
            addPhysicalDivToLink(lastMatch, missingPhysicalDiv, divContainer);
        }
    }

    /**
     * Finds the logical link to which a missing physical div should be assigned.
     * <p>
     * The returned logical link is the last logical link in traversal order that still contains
     * the preceding physical div. This represents the logical section that is active directly before
     * the missing physical div and is therefore the best interpolation target.
     *
     * @param rootLink the root logical link used as traversal start
     * @param missingPhysicalDiv the physical div that should be interpolated
     * @param precedingPhysicalDiv the physical div directly preceding the missing physical div
     * @return the logical link the missing physical div should be attached to
     * @throws StructLinkGenerationException if the preceding physical div is not linked to any logical div
     */
    protected LogicalLink getLogicalLink(LogicalLink rootLink, PhysicalSubDiv missingPhysicalDiv,
        PhysicalSubDiv precedingPhysicalDiv) {
        LogicalLink lastMatch = null;
        LogicalLink current = rootLink;

        while (current != null) {
            if (current.getPhysicalDivs().contains(precedingPhysicalDiv)) {
                lastMatch = current;
            } else if (lastMatch != null) {
                break;
            }
            current = current.next().orElse(null);
        }

        if (lastMatch == null) {
            throw new StructLinkGenerationException(
                "Could not interpolate physical div " + missingPhysicalDiv.getId()
                    + " because preceding physical div " + precedingPhysicalDiv.getId()
                    + " is not linked to any logical div.");
        }
        return lastMatch;
    }

    /**
     * Builds a new StructLink from the given root logical link.
     *
     * @param rootLink the root logical link to build from
     * @return the resulting StructLink
     */
    protected StructLink buildStructLink(LogicalLink rootLink) {
        StructLink structLink = new StructLink();
        buildStructLink(rootLink, structLink);
        return structLink;
    }

    /**
     * Recursively populates the given StructLink with smLink elements from the logical link tree.
     *
     * @param link        the current logical link to process
     * @param structLink  the StructLink to add smLink elements to
     */
    protected void buildStructLink(LogicalLink link, StructLink structLink) {
        // add own physical divs
        for (PhysicalSubDiv physicalDiv : link.getPhysicalDivs()) {
            structLink.addSmLink(new SmLink(link.getLogicalDiv().getId(), physicalDiv.getId()));
        }
        // recursive for children
        for (LogicalLink childLink : link.getChildren()) {
            buildStructLink(childLink, structLink);
        }
    }

    /**
     * Adds a physical sub-div to the given logical link and re-sorts the physical div list.
     *
     * @param link         the logical link to update
     * @param physicalDiv  the physical sub-div to add
     * @param divContainer the physical div container used for sort order
     */
    protected static void addPhysicalDivToLink(LogicalLink link, PhysicalSubDiv physicalDiv, PhysicalDiv divContainer) {
        List<PhysicalSubDiv> physicalDivs = link.getPhysicalDivs();
        physicalDivs.add(physicalDiv);
        sortPhysicalDivs(divContainer, physicalDivs);
    }

    /**
     * Sorts the given list of physical sub-divs by their order attribute or container index.
     *
     * @param divContainer  the physical div container providing the reference order
     * @param physicalDivs  the list of physical sub-divs to sort in place
     */
    protected static void sortPhysicalDivs(PhysicalDiv divContainer, List<PhysicalSubDiv> physicalDivs) {
        if (!physicalDivs.isEmpty()) {
            physicalDivs.sort((div1, div2) -> {
                if (div1.getOrder() != null && div2.getOrder() != null) {
                    return div1.getOrder().compareTo(div2.getOrder());
                }
                Integer index1 = divContainer.getChildren().indexOf(div1);
                Integer index2 = divContainer.getChildren().indexOf(div2);
                return index1.compareTo(index2);
            });
        }
    }

    /**
     * Represents a logical div with the additional information of the parent and linked physical divs.
     */
    protected static class LogicalLink {

        private final LogicalDiv logicalDiv;

        private final List<PhysicalSubDiv> physicalDivs;

        private final List<LogicalLink> children;

        private final LogicalLink previous;

        private LogicalLink next;

        LogicalLink(LogicalLink previous, LogicalDiv logicalDiv, List<PhysicalSubDiv> physicalSubDivs) {
            this.previous = previous;
            if (this.previous != null) {
                this.previous.next = this;
            }
            this.children = new ArrayList<>();
            this.logicalDiv = logicalDiv;
            this.physicalDivs = physicalSubDivs;
        }

        /**
         * Returns the logical div associated with this link.
         *
         * @return the logical div
         */
        public LogicalDiv getLogicalDiv() {
            return logicalDiv;
        }

        /**
         * Returns the list of physical sub-divs linked to this logical div.
         *
         * @return the list of physical sub-divs
         */
        public List<PhysicalSubDiv> getPhysicalDivs() {
            return physicalDivs;
        }

        /**
         * Returns the child logical links.
         *
         * @return the list of child links
         */
        public List<LogicalLink> getChildren() {
            return children;
        }

        /**
         * Adds a child logical link.
         *
         * @param link the child link to add
         */
        public void add(LogicalLink link) {
            this.children.add(link);
        }

        /**
         * Returns the next link in hierarchy. The next link is calculated by the following order:
         *
         * <ol>
         *     <li>the first child</li>
         *     <li>the following sibling</li>
         *     <li>the following sibling of the parent</li>
         * </ol>
         * 
         * Using this method you can iterate through all links and visit every link just once.
         *
         * @return the optional next link
         */
        public Optional<LogicalLink> next() {
            return Optional.ofNullable(this.next);
        }

        /**
         * Returns the previous link in hierarchy. See {@link #next()} for a description.
         *
         * @return the optional previous link
         */
        public Optional<LogicalLink> previous() {
            return Optional.ofNullable(this.previous);
        }

        /**
         * Returns the last Link in the hierarchy.
         * 
         * @return the last link
         */
        public LogicalLink last() {
            LogicalLink last = this;
            while (last.next != null) {
                last = last.next;
            }
            return last;
        }

        /**
         * Returns a list of all links where the physicalDiv list is empty. Includes this link itself and all descendants.
         * 
         * @return links which are actually not linked
         */
        public List<LogicalLink> emptyLinks() {
            ArrayList<LogicalLink> emptyLinks = new ArrayList<>();
            if (this.physicalDivs.isEmpty()) {
                emptyLinks.add(this);
            }
            children.forEach(child -> emptyLinks.addAll(child.emptyLinks()));
            return emptyLinks;
        }

        /**
         * Runs through the preceding siblings (and their children) to return the closest physical div. If the
         * preceding siblings don't have any physical div, the parent link is checked till the root element is reached.
         *
         * @return optional of a physical sub div
         */
        public Optional<PhysicalSubDiv> findLastPhysicalDivOfDescendantsAndPrecedingSiblings() {
            LogicalLink previous = this.previous;
            while (previous != null) {
                List<PhysicalSubDiv> physicalDivs = previous.getPhysicalDivs();
                if (!physicalDivs.isEmpty()) {
                    return Optional.of(physicalDivs.getLast());
                }
                previous = previous.previous;
            }
            return Optional.empty();
        }

        /**
         * Returns the physical div of this link or if not available of the ancestors.
         *
         * @return optional of a physical sub div
         */
        public Optional<PhysicalSubDiv> findFindPhysicalDivOfAncestorsOrSelf() {
            List<PhysicalSubDiv> physicalDivs = this.getPhysicalDivs();
            if (physicalDivs.isEmpty()) {
                return findFirstPhysicalDivOfAncestors();
            }
            return Optional.of(physicalDivs.getFirst());
        }

        /**
         * Runs through all the ancestor links of this link and tries to find a physical sub div.
         *
         * @return optional of a physical sub div
         */
        public Optional<PhysicalSubDiv> findFirstPhysicalDivOfAncestors() {
            List<LogicalLink> children = new ArrayList<>(getChildren());
            for (LogicalLink child : children) {
                Optional<PhysicalSubDiv> ancestorPhysicalDivOptional = child
                    .findFindPhysicalDivOfAncestorsOrSelf();
                if (ancestorPhysicalDivOptional.isPresent()) {
                    return ancestorPhysicalDivOptional;
                }
            }
            return Optional.empty();
        }

        /**
         * Returns a set of all physical divs of this link and all its children.
         *
         * @return set of physical divs
         */
        public Set<PhysicalSubDiv> getAllPhysicalDivs() {
            Set<PhysicalSubDiv> allPhysicalDivs = new HashSet<>(this.physicalDivs);
            this.children.forEach(child -> allPhysicalDivs.addAll(child.getAllPhysicalDivs()));
            return allPhysicalDivs;
        }

        /**
         * Creates a root LogicalLink from the given logical structure map and linked map.
         *
         * @param logicalStructMap the logical structure map
         * @param linkedMap        the map of logical divs to their physical sub-divs
         * @return the root logical link
         */
        public static LogicalLink of(LogicalStructMap logicalStructMap,
            Map<LogicalDiv, List<PhysicalSubDiv>> linkedMap) {
            LogicalDiv divContainer = logicalStructMap.getDivContainer();
            return of(null, divContainer, linkedMap);
        }

        /**
         * Creates a LogicalLink for the given div, recursively building child links.
         *
         * @param previous  the preceding logical link in traversal order, or null
         * @param div       the logical div for this link
         * @param linkedMap the map of logical divs to their physical sub-divs
         * @return the created logical link
         */
        public static LogicalLink of(LogicalLink previous, LogicalDiv div,
            Map<LogicalDiv, List<PhysicalSubDiv>> linkedMap) {
            List<PhysicalSubDiv> physicalSubDivs = linkedMap.get(div);
            physicalSubDivs = physicalSubDivs != null ? physicalSubDivs : new ArrayList<>();
            LogicalLink link = new LogicalLink(previous, div, physicalSubDivs);
            LogicalLink newPrevious = link;
            for (LogicalDiv childDiv : div.getChildren()) {
                LogicalLink childLink = of(newPrevious, childDiv, linkedMap);
                link.add(childLink);
                newPrevious = childLink.last();
            }
            return link;
        }

        @Override
        public String toString() {
            return this.logicalDiv.getId();
        }
    }

}
