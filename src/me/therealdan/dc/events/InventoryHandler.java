package me.therealdan.dc.events;

import me.therealdan.dc.DeluxeColors;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

/**
 * Created by Daniel on 14/01/2017.
 */
public class InventoryHandler implements Listener {

    public DeluxeColors m;

    public InventoryHandler(DeluxeColors deluxeColors) {
        m = deluxeColors;
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        m.colorPicker.onClick(m, event);
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        m.colorPicker.onClose(m, event);
    }

}