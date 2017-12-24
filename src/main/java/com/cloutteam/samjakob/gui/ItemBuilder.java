/*
 * ItemBuilder.java
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

package com.cloutteam.samjakob.gui;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

public class ItemBuilder {

    private ItemStack stack;

    /**
     * Create/Modify custom {@link ItemStack}s easily.
     * If you want to create rather than modify an ItemStack,
     * use {@link ItemBuilder#start(Material)} instead.
     *
     * <br>
     *
     * It supports shorthand for setting the following attributes:
     * <ul>
     *     <li>Name</li>
     *     <li>Amount</li>
     *     <li>Lore</li>
     *     <li>Data / Durability</li>
     * </ul>
     *
     * @param stack The ItemStack you want to modify.
     */
    public ItemBuilder(ItemStack stack){
        this.stack = stack;
    }

    /**
     * Create a custom {@link ItemStack} easily.
     *
     * @param material The {@link Material} of the ItemStack that you want to create.
     * @return A new {@link ItemBuilder} object.
     */
    public static ItemBuilder start(Material material){
        return new ItemBuilder(new ItemStack(material));
    }

    /**
     * Sets the display name of the item.
     * Color Codes are supported (and should be prepended with an ampersand [&amp;]; e.g. &amp;c for red.)
     *
     * @param name The desired name of the ItemStack.
     * @return The updated {@link ItemBuilder} object.
     */
    public ItemBuilder name(String name){
        ItemMeta stackMeta = stack.getItemMeta();
        stackMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        stack.setItemMeta(stackMeta);
        return this;
    }

    /**
     * Sets the amount of the item.
     *
     * @param amount The desired amount of the ItemStack.
     * @return The updated {@link ItemBuilder} object.
     */
    public ItemBuilder amount(int amount){
        stack.setAmount(amount);
        return this;
    }

    /**
     * Sets the lore of the item. (Shorthand - unlimited argument version)
     *
     * <br>
     *
     * Color Codes are supported (and should be prepended with an ampersand [&amp;]; e.g. &amp;c for red.)
     * <b>Note:</b> Each argument will be a line of the lore.
     *
     * @param lore The desired lore of the item.
     * @return The updated {@link ItemBuilder} object.
     */
    public ItemBuilder lore(String... lore){
        for(int i = 0; i < lore.length; i++){
            lore[i] = ChatColor.translateAlternateColorCodes('&', lore[i]);
        }

        ItemMeta stackMeta = stack.getItemMeta();
        stackMeta.setLore(Arrays.asList(lore));
        stack.setItemMeta(stackMeta);
        return this;
    }

    /**
     * Sets the lore of the item. (Reference - {@link List<String>} version)
     * This essentially just wrapper for {@link ItemMeta#setLore(List)}, however, with color replacement.
     *
     * <br>
     *
     * Color Codes are supported (and should be prepended with an ampersand [&amp;]; e.g. &amp;c for red.)
     *
     * @param lore The desired lore of the item.
     * @return The updated {@link ItemBuilder} object.
     */
    public ItemBuilder lore(List<String> lore){
        for(int i = 0; i < lore.size(); i++){
            lore.set(i, ChatColor.translateAlternateColorCodes('&', lore.get(i)));
        }

        ItemMeta stackMeta = stack.getItemMeta();
        stackMeta.setLore(lore);
        stack.setItemMeta(stackMeta);
        return this;
    }

    /**
     * An alias for {@link ItemBuilder#durability(short)}.
     * Intended to improve readability of code.
     *
     * @param data The desired data value (durability) of the item.
     * @return The updated {@link ItemBuilder} object.
     */
    public ItemBuilder data(short data){
        stack.setDurability(data);
        return this;
    }

    /**
     * Sets the durability of the item.
     *
     * @param durability The desired durability of the item.
     * @return The updated {@link ItemBuilder} object.
     */
    public ItemBuilder durability(short durability){
        stack.setDurability(durability);
        return this;
    }

    /**
     * Returns the class' internally modified {@link ItemStack} object.
     *
     * @return The updated ItemStack.
     */
    public ItemStack build(){
        return stack;
    }

}
