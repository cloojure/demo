package demo;

import java.util.ArrayList;
public class Solution {
  public static final int MAX=150;
  public static double pi_ovr_4 = Math.PI / 4;

  public static class Point {
    public double x;
    public double y;
    public Point(double x, double y) {
      this.x = x;
      this.y = y;
    }
  }
  public static class Coords {
    public double dist;
    public double angle;
    public Coords( double dist,  double angle) {
      this.dist = dist;
      this.angle = angle;
    }
  }

  public static Coords calcCoords( Point start, Point end ) {
    double dx = end.x - start.x;
    double dy = end.y - start.y;
    double angle = Math.atan2( dy, dx );
    double dist = Math.sqrt( dx*dx + dy*dy );
    return new Coords( dist, angle );
  }

  public static boolean approx(double x, double y) {
    double delta = Math.abs( x - y );
    return delta < 1e-8;
  }

  public String solution(int AX, int AY, int BX, int BY) {
    ArrayList<Point> points = new ArrayList<Point>();
    for (int i = -MAX; i <= MAX; i++) {
      for (int j = -MAX; j <= MAX; j++) {
        points.add( new Point( i, j ) );
      }
    }
    Point first = new Point( AX, AY );
    Point second = new Point( BX, BY );
    Coords initialCoords = calcCoords( first, second );
    double nextAngle = initialCoords.angle - pi_ovr_4;
    Point dummy = new Point( MAX, MAX );
    for (Point point : points ) {

    }

    return "999, 999";
  }
}
