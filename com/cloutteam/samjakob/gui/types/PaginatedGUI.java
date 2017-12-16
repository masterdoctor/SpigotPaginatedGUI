package com.cloutteam.samjakob.gui.types;

import com.cloutteam.samjakob.gui.ItemBuilder;
import com.cloutteam.samjakob.gui.buttons.ButtonListener;
import com.cloutteam.samjakob.gui.buttons.GUIButton;
import com.cloutteam.samjakob.gui.buttons.InventoryListenerGUI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public class PaginatedGUI implements InventoryHolder {

    private Map<Integer, GUIButton> items;
    private Map<Integer, GUIButton> toolbarItems;
    private int currentPage;
    private String name;

    public PaginatedGUI(String name){
        items = new HashMap<>();
        toolbarItems = new HashMap<>();
        currentPage = 0;
        this.name = name;
    }

    public void addButton(GUIButton button){
        items.put(items.size(), button);
    }

    public void setButton(int slot, GUIButton button){
        items.put(slot, button);
    }

    @Override
    public Inventory getInventory() {
        Inventory inventory = Bukkit.createInventory(this, (getMaxPage() > 1) ? 54 : 45, name);
        // Include pagination
        GUIButton backButton = new GUIButton(ItemBuilder.start(Material.ARROW).name("&c&lPrevious Page").build());
        GUIButton pageIndicator = new GUIButton(ItemBuilder.start(Material.NAME_TAG).name("&c&lPage " + (currentPage + 1) + " of " + (getMaxPage() + 1)).build());
        GUIButton nextButton = new GUIButton(ItemBuilder.start(Material.ARROW).name("&c&lNext Page").build());

        backButton.setListener(event -> {
            PaginatedGUI menu = (PaginatedGUI) event.getClickedInventory().getHolder();
            menu.previousPage();
            event.getWhoClicked().closeInventory();
            event.getWhoClicked().openInventory(getInventory());
        });

        pageIndicator.setListener(event -> event.setCancelled(true));

        nextButton.setListener(event -> {
            PaginatedGUI menu = (PaginatedGUI) event.getClickedInventory().getHolder();
            menu.nextPage();
            event.getWhoClicked().closeInventory();
            event.getWhoClicked().openInventory(getInventory());
        });

        if(currentPage > 0)
            toolbarItems.put(3, backButton);
        if(getMaxPage() > 1)
            toolbarItems.put(4, pageIndicator);
        if(currentPage < getMaxPage())
            toolbarItems.put(5, nextButton);

        // Add all items
        int counter = 0;
        for(int key = (currentPage * 45); key < items.keySet().size(); key++){
            if(counter >= 45)
                break;
            inventory.setItem(counter, items.get(key).getItem());
            counter++;
        }

        for(int toolbarItem : toolbarItems.keySet()){
            int rawSlot = toolbarItem + 45;
            inventory.setItem(rawSlot, toolbarItems.get(toolbarItem).getItem());
        }
        return inventory;
    }

    public GUIButton getButton(int slot){
        if(slot < 45) {
            return items.get(slot);
        }else{
            return toolbarItems.get(slot - 45);
        }
    }

    public void nextPage(){
        currentPage++;
    }

    public void previousPage(){
        currentPage--;
    }

    public int getMaxPage(){
        return (int) Math.ceil(items.size() / 45);
    }

    public static void prepare(JavaPlugin plugin){
        plugin.getServer().getPluginManager().registerEvents(new InventoryListenerGUI(), plugin);
    }

}
