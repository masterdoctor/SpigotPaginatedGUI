/*
 * PaginatedGUI.java
 * Copyright (c) 2017 Sam Jakob Harker
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */

package com.cloutteam.samjakob.gui.types;

import com.cloutteam.samjakob.gui.ItemBuilder;
import com.cloutteam.samjakob.gui.buttons.GUIButton;
import com.cloutteam.samjakob.gui.buttons.InventoryListenerGUI;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
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

    private static InventoryListenerGUI inventoryListenerGUI;
    private Map<Integer, GUIButton> items;
    private Map<Integer, GUIButton> toolbarItems;
    private int currentPage;
    private String name;

    /**
     * Creates a PaginatedGUI. This is a Spigot 'Inventory Menu' that
     * will automatically add pages to accommodate additional items.
     *
     * <br>
     *
     * Color Codes are supported (and should be prepended with an
     * ampersand [&amp;]; e.g. &amp;c for red.)
     *
     * @param name The desired name of the PaginatedGUI.
     */
    public PaginatedGUI(String name){
        items = new HashMap<>();
        toolbarItems = new HashMap<>();
        currentPage = 0;
        this.name = ChatColor.translateAlternateColorCodes('&', name);
    }

    /**
     * Sets the display name of the PaginatedGUI.
     *
     * <br>
     *
     * Color Codes are supported (and should be prepended with an
     * ampersand [&amp;]; e.g. &amp;c for red.)
     *
     * @param name The desired name of the PaginatedGUI.
     */
    public void setDisplayName(String name){
        this.name = ChatColor.translateAlternateColorCodes('&', name);
    }

    /**
     * Gets the display name of the PaginatedGUI.
     *
     * <br>
     *
     * <b>Note:</b> If your inventory's display name contains
     * color codes, this will have substituted the
     * ampersands (&amp;)s with the rendering engine's
     * symbol (&sect;).
     *
     * @return The inventory's display name.
     */
    public String getDisplayName(){
        return name;
    }

    /**
     * Adds the provided {@link GUIButton} to the PaginatedGUI.
     *
     * <br>
     *
     * <b>Note:</b> This will place the button after the highest slot.
     * So if you have buttons in slot 0, 1 and 5, this will place the
     * added button in slot 6.
     *
     * @param button The button you wish to add.
     */
    public void addButton(GUIButton button){
        // Get the current maximum slot in the 'items' list.
        int slot = 0;
        for(int nextSlot : items.keySet()){
            if(nextSlot > slot){
                slot = nextSlot;
            }
        }

        // Add one to get the next maximum slot.
        slot++;

        // Put the button in that slot.
        items.put(slot, button);
    }

    /**
     * Adds the provided {@link GUIButton} but places it in the desired slot in the PaginatedGUI.
     *
     * @param slot The desired slot for the button.
     * @param button The button you wish to add.
     */
    public void setButton(int slot, GUIButton button){
        items.put(slot, button);
    }

    /**
     * Removes the {@link GUIButton} from the provided slot.
     *
     * @param slot The slot containing the button you wish to remove.
     */
    public void removeButton(int slot){
        items.remove(slot);
    }

    /**
     * Gets the {@link GUIButton} in the provided slot.
     *
     * @param slot The slot containing the GUIButton that you wish to get.
     * @return The GUIButton in the provided slot.
     */
    public GUIButton getButton(int slot){
        if(slot < 45) {
            return items.get(slot);
        }else{
            return toolbarItems.get(slot - 45);
        }
    }

    /**
     * Adds the provided {@link GUIButton} but places it in the desired slot in the PaginatedGUI's toolbar.
     *
     * @param slot The desired slot for the button.
     * @param button The button you wish to add.
     * @throws IllegalArgumentException This will occur if the slot is less than 0 or higher than 8 (as this is outside the toolbar slot range.)
     */
    public void setToolbarItem(int slot, GUIButton button) {
        if(slot < 0 || slot > 8){
            throw new IllegalArgumentException("The desired slot is outside the bounds of the toolbar slot range. [0-8]");
        }

        toolbarItems.put(slot, button);
    }

    /**
     * Removes the {@link GUIButton} from the provided slot.
     *
     * @param slot The slot containing the button you wish to remove.
     * @throws IllegalArgumentException This will occur if the slot is less than 0 or higher than 8 (as this is outside the toolbar slot range.)
     */
    public void removeToolbarItem(int slot) {
        if(slot < 0 || slot > 8){
            throw new IllegalArgumentException("The desired slot is outside the bounds of the toolbar slot range. [0-8]");
        }

        toolbarItems.remove(slot);
    }

    /**
     * Increments the current page.
     * You will need to refresh the inventory for those who have it open with {@link #refreshInventory(HumanEntity)}
     *
     * @return Whether or not the page could be changed (false when the max page is currently open as it cannot go further.)
     */
    public boolean nextPage(){
        if(currentPage < getFinalPage()){
            currentPage++;
            return true;
        }else{
            return false;
        }
    }
    /**
     * Decrements the current page.
     * You will need to refresh the inventory for those who have it open with {@link #refreshInventory(HumanEntity)}
     *
     * @return Whether or not the page could be changed (false when the first page is currently active as it cannot go further.)
     */

    public boolean previousPage(){
        if(currentPage > 0) {
            currentPage--;
            return true;
        }else{
            return false;
        }
    }

    /**
     * An alias for {@link #getFinalPage()}.
     *
     * @deprecated Use {@link #getFinalPage()} instead.
     * @return The highest page number that can be viewed.
     */
    public int getMaxPage(){
        return getFinalPage();
    }

    /**
     * Gets the number of the final page of the PaginatedGUI.
     *
     * @return The highest page number that can be viewed.
     */
    public int getFinalPage(){
        // Get the highest slot number.
        int slot = 0;
        for(int nextSlot : items.keySet()){
            if(nextSlot > slot){
                slot = nextSlot;
            }
        }

        // Add one to make the math easier.
        double highestSlot = slot + 1;

        // Divide by 45 and round up to get the page number.
        // Then subtract one to convert it to an index.
        return (int) Math.ceil(highestSlot / (double) 45) - 1;
    }

    /**
     * Simply an alias that executes {@link HumanEntity#closeInventory()} and then
     * {@link HumanEntity#openInventory(Inventory)}.
     *
     * @param holder The HumanEntity that you wish to refresh the inventory for.
     */
    public void refreshInventory(HumanEntity holder){
        holder.closeInventory();
        holder.openInventory(getInventory());
    }

    /**
     * Returns the Spigot {@link Inventory} that represents the PaginatedGUI.
     * This can then by shown to a player using {@link HumanEntity#openInventory(Inventory)}.
     *
     * <br>
     *
     * This also allows getting the PaginatedGUI instance with {@link InventoryHolder#getInventory()}.
     * Used internally ({@link InventoryListenerGUI}) to get the GUIButton and therefore listener from the raw slot.
     *
     * @return The Spigot Inventory that represents the PaginatedGUI.
     */
    @Override
    public Inventory getInventory() {
        // Create an inventory (and set an appropriate size.)
        // TODO: Allow customisation of inventory size. Maybe at first, only if the inventory is not paginated.
        Inventory inventory = Bukkit.createInventory(this, (getFinalPage() > 0) ? 54 : 45);

        /* BEGIN PAGINATION */
        GUIButton backButton = new GUIButton(ItemBuilder.start(Material.ARROW).name(PREVIOUS_PAGE).build());
        GUIButton pageIndicator = new GUIButton(ItemBuilder.start(Material.NAME_TAG)
                .name(
                        CURRENT_PAGE
                                .replaceAll(Pattern.quote("{currentPage}"), String.valueOf(currentPage + 1))
                                .replaceAll(Pattern.quote("{maxPages}"), String.valueOf(getFinalPage() + 1))
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

            refreshInventory(event.getWhoClicked());
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

            refreshInventory(event.getWhoClicked());
        });
        /* END PAGINATION */

        // Where appropriate, include pagination.
        if(currentPage > 0)
            toolbarItems.put(3, backButton);
        if(getFinalPage() > 0)
            toolbarItems.put(4, pageIndicator);
        if(currentPage < getFinalPage())
            toolbarItems.put(5, nextButton);

        // Add the main inventory items
        int counter = 0;
        for(int key = (currentPage * 45); key <= Collections.max(items.keySet()); key++){
            if(counter >= 45)
                break;

            if(items.containsKey(key)) {
                inventory.setItem(counter, items.get(key).getItem());
            }

            counter++;
        }

        // Finally, add the toolbar items.
        for(int toolbarItem : toolbarItems.keySet()){
            int rawSlot = toolbarItem + 45;
            inventory.setItem(rawSlot, toolbarItems.get(toolbarItem).getItem());
        }

        return inventory;
    }

    /**
     * Simply an alias to register the Inventory listeners for a certain plugin.
     * Intended to improve code readability.
     *
     * @param plugin The Spigot plugin instance that you wish to register the listeners for.
     */
    public static void prepare(JavaPlugin plugin){
        if(inventoryListenerGUI == null){
            inventoryListenerGUI = new InventoryListenerGUI();
            plugin.getServer().getPluginManager().registerEvents(inventoryListenerGUI, plugin);
        }
    }

}
