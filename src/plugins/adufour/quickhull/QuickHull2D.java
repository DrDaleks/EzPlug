package plugins.adufour.quickhull;

import java.util.ArrayList;
import java.util.Random;

public class QuickHull2D
{
    public static final long serialVersionUID = 1L;
    
    // variables
    Random                   rnd;
    int                      pNum             = 100;
    int                      xPoints[];
    int                      yPoints[];
    public int                      xPoints2[];
    public int                      yPoints2[];
    public int                      num;
    
    public QuickHull2D(int[] xPoints, int[] yPoints, int pNum)
    {
        rnd = new Random();
        this.pNum = pNum;
        this.xPoints = xPoints;
        this.yPoints = yPoints;
        num = 0;
        xPoints2 = new int[pNum];
        yPoints2 = new int[pNum];
        
        quickconvexhull();
    }
    
    // check whether point p is right of line ab
    public int right(int a, int b, int p)
    {
        return (xPoints[a] - xPoints[b]) * (yPoints[p] - yPoints[b]) - (xPoints[p] - xPoints[b]) * (yPoints[a] - yPoints[b]);
    }
    
    // square distance point p to line ab
    public float distance(int a, int b, int p)
    {
        float x, y, u;
        u = (((float) xPoints[p] - (float) xPoints[a]) * ((float) xPoints[b] - (float) xPoints[a]) + ((float) yPoints[p] - (float) yPoints[a]) * ((float) yPoints[b] - (float) yPoints[a]))
                / (((float) xPoints[b] - (float) xPoints[a]) * ((float) xPoints[b] - (float) xPoints[a]) + ((float) yPoints[b] - (float) yPoints[a]) * ((float) yPoints[b] - (float) yPoints[a]));
        x = (float) xPoints[a] + u * ((float) xPoints[b] - (float) xPoints[a]);
        y = (float) yPoints[a] + u * ((float) yPoints[b] - (float) yPoints[a]);
        return ((x - (float) xPoints[p]) * (x - (float) xPoints[p]) + (y - (float) yPoints[p]) * (y - (float) yPoints[p]));
    }
    
    public int farthestpoint(int a, int b, ArrayList<Integer> al)
    {
        float maxD, dis;
        int maxP, p;
        maxD = -1;
        maxP = -1;
        for (int i = 0; i < al.size(); i++)
        {
            p = al.get(i);
            if ((p == a) || (p == b)) continue;
            dis = distance(a, b, p);
            if (dis > maxD)
            {
                maxD = dis;
                maxP = p;
            }
        }
        return maxP;
    }
    
    public void quickhull(int a, int b, ArrayList<Integer> al)
    {
        if (al.size() == 0) return;
        
        int c, p;
        
        c = farthestpoint(a, b, al);
        
        ArrayList<Integer> al1 = new ArrayList<Integer>();
        ArrayList<Integer> al2 = new ArrayList<Integer>();
        
        for (int i = 0; i < al.size(); i++)
        {
            p = al.get(i);
            if ((p == a) || (p == b)) continue;
            if (right(a, c, p) > 0)
                al1.add(p);
            else if (right(c, b, p) > 0) al2.add(p);
        }
        
        quickhull(a, c, al1);
        xPoints2[num] = xPoints[c];
        yPoints2[num] = yPoints[c];
        num++;
        quickhull(c, b, al2);
    }
    
    public void quickconvexhull()
    {
        // find two points: right (bottom) and left (top)
        int r, l;
        r = l = 0;
        for (int i = 1; i < pNum; i++)
        {
            if ((xPoints[r] > xPoints[i]) || (xPoints[r] == xPoints[i] && yPoints[r] > yPoints[i])) r = i;
            if ((xPoints[l] < xPoints[i]) || (xPoints[l] == xPoints[i] && yPoints[l] < yPoints[i])) l = i;
        }
        
        // System.out.println("l: "+l+", r: "+r);
        
        ArrayList<Integer> al1 = new ArrayList<Integer>();
        ArrayList<Integer> al2 = new ArrayList<Integer>();
        
        int upper;
        for (int i = 0; i < pNum; i++)
        {
            if ((i == l) || (i == r)) continue;
            upper = right(r, l, i);
            if (upper > 0)
                al1.add(i);
            else if (upper < 0) al2.add(i);
        }
        
        xPoints2[num] = xPoints[r];
        yPoints2[num] = yPoints[r];
        num++;
        quickhull(r, l, al1);
        xPoints2[num] = xPoints[l];
        yPoints2[num] = yPoints[l];
        num++;
        quickhull(l, r, al2);
    }
}