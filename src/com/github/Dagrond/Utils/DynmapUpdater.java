package com.github.Dagrond.Utils;

import java.util.List;

import org.dynmap.markers.AreaMarker;
import org.dynmap.markers.MarkerAPI;
import org.dynmap.markers.MarkerSet;

import com.github.Dagrond.NationPlugin;
import com.github.Dagrond.Nation.Estate;
import com.sk89q.worldedit.BlockVector2D;

public class DynmapUpdater {

	public DynmapUpdater(NationPlugin plugin) {
		MarkerAPI marker = plugin.getDynmap().getMarkerAPI();
		MarkerSet set = marker.getMarkerSet("Prowincje");
		if (set != null)
			set.deleteMarkerSet();
		set = marker.createMarkerSet("Prowincje", "Prowincje", null, false);
		for (Estate e : Estate.getEstates()) {
			List<BlockVector2D> points = e.getRegion().getPoints();
			double[] x = new double[points.size()];
			double[] z = new double[points.size()];
			for (int i = 0; i < points.size(); i++) {
				BlockVector2D pt = points.get(i);
				x[i] = pt.getX();
				z[i] = pt.getZ();
			}
			String markerid = e.getWorld().getName() + "_" + e.getRegion().getId();
			set.getMarkers().remove(set.findMarker(e.toString()));
			AreaMarker m = set.createAreaMarker(markerid, e.toString(), false, e.getWorld().getName(), x, z, false);
			m.setCornerLocations(x, z);
			m.setLabel(e.toString());
			String desc = "<div class=\"infowindow\"><span style=\"font-size:120%;\"><b>" + e.toString()
					+ "</b></span><br /> " + msg.get("raw_nation", false) + ": "
					+ (e.isOccupied() ? e.getNation().toString() : msg.get("raw_not_claimed", false)) + "";
			if (!e.getDescription().isEmpty()) {
				for (String line : e.getDescription()) {
					desc += line + "<br />";
				}
			}
			desc += "</div>";
			m.setDescription(desc);
			m.setFillStyle(0.5, e.getNation() == null ? 0x7c7c77 : e.getNation().getHEXcolor());
			m.setLineStyle(1, 1, e.getNation() == null ? 0x7c7c77 : e.getNation().getHEXcolor());
		}
	}
}
