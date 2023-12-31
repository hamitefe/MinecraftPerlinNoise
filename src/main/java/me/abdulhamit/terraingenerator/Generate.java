package me.abdulhamit.terraingenerator;

import de.articdive.jnoise.generators.noise_parameters.fade_functions.FadeFunction;
import de.articdive.jnoise.generators.noise_parameters.interpolation.Interpolation;
import de.articdive.jnoise.pipeline.JNoise;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Random;

public class Generate implements CommandExecutor {
    public static int seed = new Random().nextInt(5000);
    private static JNoise noisePipeline = JNoise.
            newBuilder()
            .scale(40.0)
            .perlin(seed, Interpolation.COSINE, FadeFunction.SMOOTHSTEP)
            .build();
    
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        try {
            double smoothness = Double.parseDouble(args[0]);
            double height = Double.parseDouble(args[1]);
            int waterHeight = Integer.parseInt(args[2]);
            int size = Integer.parseInt(args[3]);
            
            generateTerrain((Player) commandSender, smoothness, height,waterHeight, size);
        } catch (Exception e){
            commandSender.sendMessage("Error executing command: "+e.getMessage());
        }
        
        
        
        return true;
    }
    
    
    public static void generateTerrain(Player p, double smoothness, double yscale, int waterheight, int size){
        final Location loc = p.getLocation().clone();
        final World world = loc.getWorld();
        long startTick = System.currentTimeMillis();
        for (int x = -size; x<size; x++){
            for (int z = -size; z<size; z++){
                int height = (int) Math.round(noisePipeline.evaluateNoise((loc.getX()+x)/smoothness, (loc.getZ()+z)/smoothness)*yscale);
                int grassHeight = height;
                int dirtHeight = grassHeight-3;
                for (int y = (height<waterheight) ? waterheight : height; y >=-20; y--){
                    Block block = new Location(world,loc.getX()+x, y, loc.getZ()+z).getBlock();
                    if (y<=height) {
                        if (y == grassHeight&&y>waterheight-1) {
                            block.setType(Material.GRASS_BLOCK);
                            placeFlower(block.getRelative(0, 1, 0));
                        } else if (y >= dirtHeight&&y>waterheight-1) {
                            block.setType(Material.DIRT);
                        }else {
                            block.setType(Material.STONE);
                        }
                    } else if(y<=waterheight) {
                        block.setType(Material.WATER);
                    } else {
                        block.setType(Material.AIR);
                    }
                }
            }
        }
        
        p.sendMessage("generated in "+(System.currentTimeMillis()-startTick)+" milli seconds");
    }
    
    public static void resetSeed(){
        seed = new Random().nextInt(5000);
        noisePipeline = JNoise.
                newBuilder()
                .scale(40.0)
                .perlin(seed, Interpolation.COSINE, FadeFunction.SMOOTHSTEP)
                .build();
    }
    
    private static void placeFlower(Block b){
        List<Material> blocks = List.of(
                Material.GRASS,
                Material.AIR
        );
        b.setType(blocks.get(new Random().nextInt(blocks.size())));
    }
}
