package com.ARSW.parcial;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Bolsa {
    private String Information;
    private String symbol;
    private String Refreshed;
    private String Interval;
    private String  Size;
    private String Time;
    private List<Point> points = new ArrayList<>();

    public Bolsa() { }

    public Bolsa(String Information, String symbol, String Refreshed, String Interval, String Size, String Time, Point[] pts) {
        this.Information = Information;
        this.symbol = symbol;
        this.Refreshed = Refreshed;
        this.Interval = Interval;
        this.Size = Size;
        this.Time = Time;

        if (pts != null) {
            points.addAll(Arrays.asList(pts));
        }
    }

    public String getInformation() { return Information; }
    public String getSymbol() { return symbol; }
    public String getRefreshed() { return Refreshed; }
    public String getInterval() { return Interval; }
    public String getSize() { return Size ; }
    public String getTime() { return Time; }

    public List<Point> getPoints() { return points; }

    public void setAuthor(String Information) { this.Information = Information; }
    public void setName(String symbol) { this.symbol = symbol; }
    public void setRefreshed(String Refreshed) { this.Refreshed = Refreshed; }
    public void setInterval(String Interval) { this.Interval = Interval; }
    public void setSize(String Size) { this.Size = Size; }
    public void setTime(String Time) { this.Time = Time; }
    public void setPoints(List<Point> points) { this.points = points; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Bolsa)) return false;
        Bolsa that = (Bolsa) o;
        return Objects.equals(Information, that.Information);
               Objects.equals(symbol, that.symbol);
               Objects.equals(Refreshed, that.Refreshed);
               Objects.equals(Interval, that.Interval);
               Objects.equals(Size, that.Size);
               Objects.equals(Time, that.Time);
                


    }
    @Override public int hashCode() { return Objects.hash(Information, symbol); }
}