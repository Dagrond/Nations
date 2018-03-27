package com.gmail.ZiomuuSs.Utils;

import java.util.List;

import org.dynmap.markers.AreaMarker;
import org.dynmap.markers.MarkerAPI;
import org.dynmap.markers.MarkerSet;

import com.gmail.ZiomuuSs.NationPlugin;
import com.gmail.ZiomuuSs.Nation.Estate;
import com.sk89q.worldedit.BlockVector2D;

public class DynmapUpdater {
  
  public DynmapUpdater(NationPlugin plugin) {
    MarkerAPI marker = plugin.getDynmap().getMarkerAPI();
    MarkerSet set = marker.getMarkerSet("nations");
    if (set != null) set.deleteMarkerSet();
    set = marker.createMarkerSet("nations", "nations", null, false);
    for (Estate e : Estate.getEstates()) {
      List<BlockVector2D> points = e.getRegion().getPoints();
      double[] x = new double[points.size()];
      double[] z = new double[points.size()];
      for(int i = 0; i < points.size(); i++) {
          BlockVector2D pt = points.get(i);
          x[i] = pt.getX();
          z[i] = pt.getZ();
      }
      String markerid = e.getRegionWorld().getName() + "_" + e.getRegion().getId();
      set.getMarkers().remove(set.findMarker(e.toString()));
      AreaMarker m = set.createAreaMarker(markerid, e.toString(), false, e.getRegionWorld().getName(), x, z, false);
      m.setCornerLocations(x, z);
      m.setLabel(e.toString());
      String desc = "\"<div class=\"infowindow\"><span style=\"font-size:120%;\"><b>"+e.toString()+"</b></span><br /> "+msg.get("nation", false)+": "+(e.getNation() == null ? msg.get("not_claimed", false) : e.getNation().toString())+"";
      if (!e.getDescription().isEmpty()) {
        for (String line : e.getDescription()) {
          desc += line+"<br />";
        }
      }
      desc += "</div>";
      m.setDescription(desc);
      m.setFillStyle(0.8, e.getNation() == null ? 0xFF0000 : e.getNation().getColor());
      m.setLineStyle(1, 1, e.getNation() == null ? 0xFF0000 : e.getNation().getColor());
    }
  }
}
