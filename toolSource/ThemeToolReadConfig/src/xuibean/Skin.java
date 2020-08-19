package xuibean;

import java.util.List;

public class Skin {
    private List<Page> pages;

    @Override
    public String toString() {
        return "Skin{" +
                "pages=" + pages +
                '}';
    }

    public List<Page> getPages() {
        return pages;
    }

    public void setPages(List<Page> pages) {
        this.pages = pages;
    }

    public static class Page {
        private int pageIndex;
        private List<App> apps;

        public int getPageIndex() {
            return pageIndex;
        }

        public List<App> getApps() {
            return apps;
        }

        public void setPageIndex(int pageIndex) {
            this.pageIndex = pageIndex;
        }

        public void setApps(List<App> apps) {
            this.apps = apps;
        }

        @Override
        public String toString() {
            return "Page{" +
                    "pageIndex=" + pageIndex +
                    ", apps=" + apps +
                    '}';
        }
    }

    public static class App {
        private int index;
        private int x;
        private int y;
        private int icon_size;//如果为0，wrap_content
        private int label_relative_loc;//(左-1)(下0)(右1)(上2)（-2不显示）
        private int margin_text_icon;//(图标-图标名距离)
        private String text_color;//图标名称颜色
        private int icon_padding;//点击范围扩大值。要考虑对x,y,margin_text_icon的影响。或许实际x,y为(x - icon_padding, y - icon_padding)

        public int getIndex() {
            return index;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public int getIcon_size() {
            return icon_size;
        }

        public int getLabel_relative_loc() {
            return label_relative_loc;
        }

        public int getMargin_text_icon() {
            return margin_text_icon;
        }

        public String getText_color() {
            return text_color;
        }

        public int getIcon_padding() {
            return icon_padding;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public void setX(int x) {
            this.x = x;
        }

        public void setY(int y) {
            this.y = y;
        }

        public void setIcon_size(int icon_size) {
            this.icon_size = icon_size;
        }

        public void setLabel_relative_loc(int label_relative_loc) {
            this.label_relative_loc = label_relative_loc;
        }

        public void setMargin_text_icon(int margin_text_icon) {
            this.margin_text_icon = margin_text_icon;
        }

        public void setText_color(String text_color) {
            this.text_color = text_color;
        }

        public void setIcon_padding(int icon_padding) {
            this.icon_padding = icon_padding;
        }

        @Override
        public String toString() {
            return "App{" +
                    "index=" + index +
                    ", x=" + x +
                    ", y=" + y +
                    ", icon_size=" + icon_size +
                    ", label_relative_loc=" + label_relative_loc +
                    ", margin_text_icon=" + margin_text_icon +
                    ", text_color=" + text_color +
                    ", icon_padding=" + icon_padding +
                    '}';
        }
    }
}
