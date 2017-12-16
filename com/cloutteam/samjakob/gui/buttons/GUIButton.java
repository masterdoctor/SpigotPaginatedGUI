package com.cloutteam.samjakob.gui.buttons;

import org.bukkit.inventory.ItemStack;

public class GUIButton {

    private ButtonListener listener;
    private ItemStack item;

    public GUIButton(ItemStack item){
        this.item = item;
    }

    public void setListener(ButtonListener listener){
        this.listener = listener;
    }

    public ButtonListener getListener(){
        return listener;
    }

    public ItemStack getItem(){
        return item;
    }


}
