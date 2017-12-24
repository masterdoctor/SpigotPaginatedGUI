package com.cloutteam.samjakob.gui.buttons;

import com.cloutteam.samjakob.gui.types.PaginatedGUI;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryListenerGUI implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        // Determine if the Inventory was a PaginatedGUI
        if (event.getInventory().getHolder() != null
                && event.getInventory().getHolder() instanceof PaginatedGUI) {

            // Get the instance of the PaginatedGUI that was clicked.
            PaginatedGUI paginatedGUI = (PaginatedGUI) event.getInventory().getHolder();

            // Then, assume the slot holds a GUIButton and attempt to get the button.
            GUIButton button = paginatedGUI.getButton(event.getSlot());

            // Finally, if the slot did actually hold a GUIButton (that has a listener)...
            if (button != null && button.getListener() != null) {
                // ...execute that button's listener.
                button.getListener().onClick(event);
            }

        }
    }

}
