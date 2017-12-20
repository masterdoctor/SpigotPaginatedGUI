package com.cloutteam.samjakob.gui.types;

import com.cloutteam.samjakob.gui.ItemBuilder;
import com.cloutteam.samjakob.gui.buttons.GUIButton;
import com.cloutteam.samjakob.gui.buttons.InventoryListenerGUI;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class PaginatedGUI implements InventoryHolder {

    /* BEGIN: CONFIGURATION */
    private static final String CHAT_PREFIX = "&c&lGUI  &c";
    private static final String NO_PREVIOUS_PAGES = "There are no previous pages.";
    private static final String NO_ADDITIONAL_PAGES = "There are no additional pages.";

    private static final String PREVIOUS_PAGE = "&c&lPrevious Page";
    private static final String CURRENT_PAGE = "&c&lPage {currentPage} of {maxPages}";
    private static final String NEXT_PAGE = "&c&lNext Page";
    /* END: CONFIGURATION */

    private Map<Integer, GUIButton> items;
    private Map<Integer, GUIButton> toolbarItems;
    private int currentPage;
    private String name;

    public PaginatedGUI(String name){
        items = new HashMap<>();
        toolbarItems = new HashMap<>();
        currentPage = 0;
        this.name = ChatColor.translateAlternateColorCodes('&', name);
    }

    public void addButton(GUIButton button){
        items.put(items.size(), button);
    }

    public void setButton(int slot, GUIButton button){
        items.put(slot, button);
    }

    public void setToolbarItem(int slot, GUIButton button) {
        toolbarItems.put(slot, button);
    }
    
    public void removeToolbarItem(int slot) {
        toolbarItems.remove(slot);
    }

    @Override
    public Inventory getInventory() {
        Inventory inventory = Bukkit.createInventory(this, (getMaxPage() > 0) ? 54 : 45, name);
        // Include pagination
        GUIButton backButton = new GUIButton(ItemBuilder.start(Material.ARROW).name(PREVIOUS_PAGE).build());
        GUIButton pageIndicator = new GUIButton(ItemBuilder.start(Material.NAME_TAG)
                .name(
                        CURRENT_PAGE
                                .replaceAll(Pattern.quote("{currentPage}"), String.valueOf(currentPage + 1))
                                .replaceAll(Pattern.quote("{maxPages}"), String.valueOf(getMaxPage() + 1))
                )
                .build());
        GUIButton nextButton = new GUIButton(ItemBuilder.start(Material.ARROW).name(NEXT_PAGE).build());

        backButton.setListener(event -> {
            event.setCancelled(true);
            PaginatedGUI menu = (PaginatedGUI) event.getClickedInventory().getHolder();

            if(!menu.previousPage()){
                event.getWhoClicked().sendMessage(ChatColor.translateAlternateColorCodes('&',
                        CHAT_PREFIX + NO_PREVIOUS_PAGES));
                return;
            }

            event.getWhoClicked().closeInventory();
            event.getWhoClicked().openInventory(getInventory());
        });

        pageIndicator.setListener(event -> event.setCancelled(true));

        nextButton.setListener(event -> {
            event.setCancelled(true);
            PaginatedGUI menu = (PaginatedGUI) event.getClickedInventory().getHolder();

            if(!menu.nextPage()){
                event.getWhoClicked().sendMessage(ChatColor.translateAlternateColorCodes('&',
                        CHAT_PREFIX + NO_ADDITIONAL_PAGES));
                return;
            }

            event.getWhoClicked().closeInventory();
            event.getWhoClicked().openInventory(getInventory());
        });

        if(currentPage > 0)
            toolbarItems.put(3, backButton);
        if(getMaxPage() > 0)
            toolbarItems.put(4, pageIndicator);
        if(currentPage < getMaxPage())
            toolbarItems.put(5, nextButton);

        // Add all items
        int counter = 0;
        for(int key = (currentPage * 45); key <= Collections.max(items.keySet()); key++){
            if(counter >= 45)
                break;

            if(items.containsKey(key)) {
                inventory.setItem(counter, items.get(key).getItem());
            }

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

    public boolean nextPage(){
        if(currentPage < getMaxPage()){
            currentPage++;
            return true;
        }else{
            return false;
        }
    }

    public boolean previousPage(){
        if(currentPage > 0) {
            currentPage--;
            return true;
        }else{
            return false;
        }
    }

    public int getMaxPage(){
        return items.size() % 45 == 0 ? Math.round(items.size() / 45) - 1 : (int) Math.ceil(items.size() / 45);
    }

    public static void prepare(JavaPlugin plugin){
        plugin.getServer().getPluginManager().registerEvents(new InventoryListenerGUI(), plugin);
    }

}
