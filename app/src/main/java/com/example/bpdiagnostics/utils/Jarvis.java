package com.example.bpdiagnostics.utils;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.model.PointValue;

/**
 * Created by mykola on 11.06.17.
 */

public class Jarvis {
    private boolean CCW(PointValue p, PointValue q, PointValue r) {
        float val = (q.getY() - p.getY()) * (r.getX() - q.getX()) - (q.getX() - p.getX()) * (r.getY() - q.getY());

        if (val >= 0)
            return false;
        return true;
    }

    public List<PointValue> search(List<PointValue> points) {
        int n = points.size();

        if (n <= 3)
            return points;

        ArrayList<PointValue> next = new ArrayList<>();

        int leftMost = 0;
        for (int i = 1; i < n; i++)
            if (points.get(i).getX() < points.get(leftMost).getX())
                leftMost = i;
        int p = leftMost, q;

        next.add(points.get(p));

        do {
            q = (p + 1) % n;
            for (int i = 0; i < n; i++)
                if (CCW(points.get(p), points.get(i), points.get(q)))
                    q = i;

            next.add(points.get(q));
            p = q;
        } while (p != leftMost);

        return next;
    }


}

