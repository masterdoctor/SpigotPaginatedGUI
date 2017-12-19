package com.cloutteam.samjakob.gui.buttons;

import com.cloutteam.samjakob.gui.types.PaginatedGUI;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryListenerGUI implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event){
        if(event.getInventory().getHolder() != null) {
            if(event.getInventory().getHolder() instanceof PaginatedGUI){
                    PaginatedGUI paginatedGUI = (PaginatedGUI) event.getInventory().getHolder();

                    GUIButton button = paginatedGUI.getButton(event.getSlot());

                    if(button != null && button.getListener() != null) {
                        event.setCancelled(true);
                        button.getListener().onClick(event);
                    }
            }
        }
    }

}
