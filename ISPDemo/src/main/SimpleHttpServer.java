package src.main;


import src.main.inter.Viewer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimpleHttpServer {
    private final Map<String, List<Viewer>> viewers = new HashMap<>();

    public void addViewers(String url, Viewer viewer) {
        if (!viewers.containsKey(url)) {
            viewers.put(url, new ArrayList<>());
        }
        viewers.get(url).add(viewer);
    }

    public void run() {
        viewers.keySet().forEach(key -> {
                   List<Viewer> listViewer = viewers.get(key);
                   for (Viewer viewer : listViewer) {
                       System.out.println(viewer.outputInPlainText());
                   }
               });
    }
}
