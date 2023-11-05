 package mcp.mobius.waila.handlers;

 import mcp.mobius.waila.api.IWailaConfigHandler;
 import mcp.mobius.waila.api.IWailaSummaryProvider;
 import net.fabricmc.clienttools.lib.IShearable;
 import net.minecraft.client.Minecraft;
 import net.minecraft.src.*;

 import java.lang.reflect.Field;
 import java.util.ArrayList;
 import java.util.Collections;
 import java.util.HashMap;
 import java.util.LinkedHashMap;

 public class SummaryProviderDefault
   implements IWailaSummaryProvider
 {
   public LinkedHashMap<String, String> getSummary(ItemStack stack, LinkedHashMap<String, String> currentSummary, IWailaConfigHandler config) {
     if (!(stack.getItem() instanceof ItemBlock)) {

       if (getToolMaterialName(stack) != null) {
         currentSummary.put("Material", getToolMaterialName(stack));
         currentSummary.put("Tier", (getHarvestLevel(stack) != null) ? String.valueOf(getHarvestLevel(stack)) : null);
         currentSummary.put("Durability", (getItemDurability(stack) != null) ? String.valueOf(getItemDurability(stack)) : null);

         currentSummary.put("Harvest", getEffectiveBlock(stack));
         if (getEffectiveBlock(stack) != null) {
           currentSummary.put("Efficiency", (getEfficiencyOnProperMaterial(stack) != null) ? String.valueOf(getEfficiencyOnProperMaterial(stack)) : null);
         }
         currentSummary.put("Enchant", (getToolEnchantability(stack) != null) ? String.valueOf(getToolEnchantability(stack)) : null);
         currentSummary.put("Damage", (getDamageVsEntity(stack) != null) ? String.valueOf(getDamageVsEntity(stack)) : null);
       }
       else if (getArmorMaterialName(stack) != null) {
         currentSummary.put("Material", getArmorMaterialName(stack));
         currentSummary.put("Durability", (getItemDurability(stack) != null) ? String.valueOf(getItemDurability(stack)) : null);
         currentSummary.put("Armor Value", (getDamageReduction(stack) != null) ? String.valueOf(getDamageReduction(stack)) : null);
         currentSummary.put("Enchant", (getArmorEnchantability(stack) != null) ? String.valueOf(getArmorEnchantability(stack)) : null);
       }
     } else {

       ItemBlock itemBlock = (ItemBlock)stack.getItem();
       Block block = Block.blocksList[itemBlock.getBlockID()];
       try { currentSummary.put("Hardness", String.valueOf(block.getBlockHardness((Minecraft.getMinecraft()).theWorld, 0, 0, 0))); } catch (Exception ignored) {}
       try { currentSummary.put("Resistance", String.valueOf(block.getExplosionResistance(null))); } catch (Exception ignored) {}


//       try { ArrayList<ItemStack> droppedStacks = block.getBlockDropped((World)(Minecraft.getMinecraft()).theWorld, 0, 0, 0, stack.getItemDamage(), 3);
//         if (droppedStacks.size() == 1)
//         { currentSummary.put("Drop", ((ItemStack)droppedStacks.get(0)).s()); }
//         else
//         { for (int i = 0; i < droppedStacks.size(); i++)
//           { currentSummary.put(String.format("Drop %s", i), ((ItemStack)droppedStacks.get(i)).s()); }  }  }
//       catch (Exception e) { System.out.printf("%s\n", e); }


       try { if (block instanceof IShearable) {
           IShearable shearable = (IShearable)block;
           ArrayList<ItemStack> droppedStacks = shearable.onSheared(null, Minecraft.getMinecraft().theWorld, 0, 0, 0, 3);
           if (droppedStacks.size() == 1)
           { currentSummary.put("Sheared", (droppedStacks.get(0)).getItemName()); }
           else
           { for (int i = 0; i < droppedStacks.size(); i++)
             { currentSummary.put(String.format("Sheared %s", i), (droppedStacks.get(i)).getItemName()); }  }
         }  }
       catch (Exception e) { System.out.printf("%s\n", e); }

     }


     return currentSummary;
   }

   public EnumToolMaterial getToolMaterial(ItemStack stack) {
     Class<?> itemClass = stack.getItem().getClass();
     EnumToolMaterial material = null;
     try {
       Field getMaterial = findField(itemClass, "toolMaterial");
       if (getMaterial != null) {
         getMaterial.setAccessible(true);
         material = (EnumToolMaterial)getMaterial.get(stack.getItem());
       }
     } catch (Exception ignored) {}
     return material;
   }

   public EnumArmorMaterial getArmorMaterial(ItemStack stack) {
     Class<?> itemClass = stack.getItem().getClass();
     EnumArmorMaterial material = null;
     try {
       Field getMaterial = findField(itemClass, "material");
       if (getMaterial != null) {
         getMaterial.setAccessible(true);
         material = (EnumArmorMaterial)getMaterial.get(stack.getItem());
       }
     } catch (Exception e) {}
     return material;
   }

   public String getToolMaterialName(ItemStack stack) {
     EnumToolMaterial material = getToolMaterial(stack);
     if (material == null) {
       return null;
     }
     HashMap<Object, Object> matName = new HashMap<Object, Object>();
     matName.put("WOOD", "Wood");
     matName.put("STONE", "Stone");
     matName.put("IRON", "Iron");
     matName.put("EMERALD", "Diamond");
     matName.put("GOLD", "Gold");
     String materialName = material.toString();
     materialName = matName.containsKey(materialName) ? (String)matName.get(materialName) : materialName;
     return materialName;
   }

   public String getArmorMaterialName(ItemStack stack) {
     EnumArmorMaterial material = getArmorMaterial(stack);
     if (material == null) {
       return null;
     }
     HashMap<Object, Object> matName = new HashMap<Object, Object>();
     matName.put("CLOTH", "Cloth");
     matName.put("CHAIN", "Chain");
     matName.put("IRON", "Iron");
     matName.put("DIAMOND", "Diamond");
     matName.put("GOLD", "Gold");
     String materialName = material.toString();
     materialName = matName.containsKey(materialName) ? (String)matName.get(materialName) : materialName;
     return materialName;
   }

   public String getEffectiveBlock(ItemStack stack) {
     Class<?> itemClass = stack.getItem().getClass();
     try {
       Field blocksEffectiveField = itemClass.getField("blocksEffectiveAgainst");
       Block[] effectiveBlocksArray = (Block[])blocksEffectiveField.get(stack.getItem());
       ArrayList<? super Block> effectiveBlocks = new ArrayList<>();
       Collections.addAll(effectiveBlocks, effectiveBlocksArray);
       String effectiveAgainst = "";

       if (effectiveBlocks.contains(Block.obsidian)) {
         effectiveAgainst = Block.obsidian.getLocalizedName();
       } else if (effectiveBlocks.contains(Block.stone)) {
         effectiveAgainst = Block.stone.getLocalizedName();
       } else if (effectiveBlocks.contains(Block.dirt)) {
         effectiveAgainst = Block.dirt.getLocalizedName();
       } else if (effectiveBlocks.contains(Block.wood)) {
         effectiveAgainst = Block.wood.getLocalizedName();
       }
       return effectiveAgainst.equals("") ? null : effectiveAgainst;
     } catch (Exception e) {
       return null;
     }
   }
   public Integer getHarvestLevel(ItemStack stack) {
     EnumToolMaterial material = getToolMaterial(stack);
     return (material != null) ? material.getHarvestLevel() : null;
   }

   public Float getEfficiencyOnProperMaterial(ItemStack stack) {
     EnumToolMaterial material = getToolMaterial(stack);
     return (material != null) ? material.getEfficiencyOnProperMaterial() : null;
   }

   public Integer getToolEnchantability(ItemStack stack) {
     EnumToolMaterial material = getToolMaterial(stack);
     return (material != null) ? material.getEnchantability() : null;
   }

   public Integer getDamageVsEntity(ItemStack stack) {
     EnumToolMaterial material = getToolMaterial(stack);
     return (material != null) ? material.getDamageVsEntity() : null;
   }

   public Integer getItemDurability(ItemStack stack) {
     return (stack.getItemDamage() > 0) ? stack.getItemDamage() : null;
   }

   public Integer getDamageReduction(ItemStack stack) {
     if (stack.getItem() instanceof ItemArmor) {
       ItemArmor armor = (ItemArmor)stack.getItem();
       return armor.damageReduceAmount;
     }
     return null;
   }

   public Integer getArmorEnchantability(ItemStack stack) {
     EnumArmorMaterial material = getArmorMaterial(stack);
     return (material != null) ? material.getEnchantability() : null;
   }

   public Field findField(Class<?> lookup, String name) {
     Field retField = null;
     try {
       retField = lookup.getDeclaredField(name);
     } catch (Exception e) {
       if (lookup.getSuperclass() == null) {
         retField = null;
       } else {
         retField = findField(lookup.getSuperclass(), name);
       }
     }  return retField;
   }
 }


