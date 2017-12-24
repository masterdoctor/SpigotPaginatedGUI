package com.cloutteam.samjakob.gui.buttons;

import org.bukkit.inventory.ItemStack;

public class GUIButton {

    private ButtonListener listener;
    private ItemStack item;

    /**
     * Creates a GUIButton with the {@link ItemStack} as it's 'icon' in the inventory.
     *
     * @param item The desired 'icon' for the GUIButton.
     */
    public GUIButton(ItemStack item){
        this.item = item;
    }

    /**
     * Sets the {@link ButtonListener} that is to be executed when the GUIButton is clicked.
     *
     * @param listener The listener to be executed on button click.
     */
    public void setListener(ButtonListener listener){
        this.listener = listener;
    }

    /**
     * Returns the {@link ButtonListener} that is to be executed when the GUIButton is clicked.
     * This is mostly for internal use by the API and is likely not useful to you.
     *
     * @return The listener to be executed on button click.
     */
    public ButtonListener getListener(){
        return listener;
    }

    /**
     * Returns the {@link ItemStack} that will be used as the GUIButton's 'icon' in an inventory.
     *
     * @return The GUIButton's 'icon'.
     */
    public ItemStack getItem(){
        return item;
    }


}
