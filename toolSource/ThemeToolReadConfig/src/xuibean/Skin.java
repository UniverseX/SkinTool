package xuibean;

import java.util.List;

public class Skin {
    private List<Page> pages;
    private LiveWallPager liveWallPager;
    private WeatherClock clock;

    @Override
    public String toString() {
        return "Skin{" +
                "pages=" + pages +
                ", liveWallPager=" + liveWallPager +
                ", clock=" + clock +
                '}';
    }

    public List<Page> getPages() {
        return pages;
    }

    public LiveWallPager getLiveWallPager() {
        return liveWallPager;
    }

    public WeatherClock getClock() {
        return clock;
    }

    public void setPages(List<Page> pages) {
        this.pages = pages;
    }

    public void setLiveWallPager(LiveWallPager liveWallPager) {
        this.liveWallPager = liveWallPager;
    }

    public void setClock(WeatherClock clock) {
        this.clock = clock;
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

        public int getIndex() {
            return index;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
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

        @Override
        public String toString() {
            return "App{" +
                    "index=" + index +
                    ", x=" + x +
                    ", y=" + y +
                    '}';
        }
    }

    public static class LiveWallPager {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "LiveWallPager{" +
                    "name='" + name + '\'' +
                    '}';
        }
    }

    public static class WeatherClock {
        private String name;
        private int pageIndex;
        private int weatherTextSize;
        private int clockTextSize;
        private int locationTextSize;
        private int weatherTextColor;
        private int clockTextColor;
        private int locationTextColor;
        private int type;
        private int x;
        private int y;

        public String getName() {
            return name;
        }

        public int getPageIndex() {
            return pageIndex;
        }

        public int getWeatherTextSize() {
            return weatherTextSize;
        }

        public int getClockTextSize() {
            return clockTextSize;
        }

        public int getLocationTextSize() {
            return locationTextSize;
        }

        public int getWeatherTextColor() {
            return weatherTextColor;
        }

        public int getClockTextColor() {
            return clockTextColor;
        }

        public int getLocationTextColor() {
            return locationTextColor;
        }

        public int getType() {
            return type;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setPageIndex(int pageIndex) {
            this.pageIndex = pageIndex;
        }

        public void setWeatherTextSize(int weatherTextSize) {
            this.weatherTextSize = weatherTextSize;
        }

        public void setClockTextSize(int clockTextSize) {
            this.clockTextSize = clockTextSize;
        }

        public void setLocationTextSize(int locationTextSize) {
            this.locationTextSize = locationTextSize;
        }

        public void setWeatherTextColor(int weatherTextColor) {
            this.weatherTextColor = weatherTextColor;
        }

        public void setClockTextColor(int clockTextColor) {
            this.clockTextColor = clockTextColor;
        }

        public void setLocationTextColor(int locationTextColor) {
            this.locationTextColor = locationTextColor;
        }

        public void setType(int type) {
            this.type = type;
        }

        public void setX(int x) {
            this.x = x;
        }

        public void setY(int y) {
            this.y = y;
        }

        @Override
        public String toString() {
            return "WeatherClock{" +
                    "name='" + name + '\'' +
                    ", pageIndex=" + pageIndex +
                    ", weatherTextSize=" + weatherTextSize +
                    ", clockTextSize=" + clockTextSize +
                    ", locationTextSize=" + locationTextSize +
                    ", weatherTextColor=" + weatherTextColor +
                    ", clockTextColor=" + clockTextColor +
                    ", locationTextColor=" + locationTextColor +
                    ", type=" + type +
                    ", x=" + x +
                    ", y=" + y +
                    '}';
        }
    }
}
