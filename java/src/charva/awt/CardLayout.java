package charva.awt;

import java.util.HashMap;

/**
 * Emulates {@java.awt.CardLayout}.
 * @author lapo@lapo.it
 */
public class CardLayout implements LayoutManager2 {

    protected HashMap names   = new HashMap();
    protected int     current = -1;
    transient int     max     = 0;

    public void addLayoutComponent(Component component, Object constraints) {
        if (!(constraints instanceof String))
            throw new IllegalArgumentException("cannot add to layout: constraint must be a string");
        String name = (String) constraints;
        if (max > 0)
            component.setVisible(false); // initially only the first card may be visible
        if (name != null)
            names.put(name, Integer.valueOf(max));
        ++max;
    }

    public void invalidateLayout(Container target) {
        // this layout manager caches nothing
    }

    public Dimension minimumSize(Container container) {
        Dimension min = new Dimension(0, 0);
        for (int i = 0, j = container.getComponentCount(); i < j; ++i) {
            Dimension t = container.getComponent(i).minimumSize();
            if (t.width > min.width)
                min.width = t.width;
            if (t.height > min.height)
                min.height = t.height;
        }
        Insets insets = container.getInsets();
        min.width += insets.left + insets.right;
        min.height += insets.top + insets.bottom;
        return min;
    }

    public void doLayout(Container container) {
        Dimension size = container.getSize();
        Insets insets = container.getInsets();
        Point cardOrigin = new Point(insets.left, insets.top);
        Dimension cardSize = new Dimension(
            size.width  - (insets.left + insets.right),
            size.height - (insets.top  + insets.bottom));
        for (int i = 0, j = container.getComponentCount(); i < j; ++i) {
            Component c = container.getComponent(i);
            c.setBounds(cardOrigin, cardSize);
            if (c instanceof Container)
                ((Container) c).doLayout();
        }
    }

    /**
     * Flips to the component with the given insertion index.
     * If no such component exists, then nothing happens.
     * @param     container   the parent container in which to do the layout
     * @param     index    the component index
     * @see       charva.awt.CardLayout#addLayoutComponent(java.awt.Component, java.lang.Object)
     */
    public void show(Container container, int index) {
        for (int i = 0, j = container.getComponentCount(); i < j; ++i) {
            Component c = container.getComponent(i);
            if (c.isVisible())
                c.setVisible(false);
        }
        if (index < max) {
            current = index;
            container.getComponent(index).setVisible(true);
            container.validate();
        }
    }

    /**
     * Flips to the component that was added to this layout with the
     * specified <code>name</code>, using <code>addLayoutComponent</code>.
     * If no such component exists, then nothing happens.
     * @param     container   the parent container in which to do the layout
     * @param     name     the component name
     * @see       charva.awt.CardLayout#addLayoutComponent(java.awt.Component, java.lang.Object)
     */
    public void show(Container container, String name) {
        show(container, ((Integer) names.get(name)).intValue());
    }

    /**
     * Flips to the first card of the container.
     * @param     container   the parent container in which to do the layout
     * @see       charva.awt.CardLayout#last
     */
    public void first(Container container) {
        show(container, 0);
    }

    /**
     * Flips to the last card of the container.
     * @param     container   the parent container in which to do the layout
     * @see       charva.awt.CardLayout#first
     */
    public void last(Container container) {
        show(container, max - 1);
    }

}