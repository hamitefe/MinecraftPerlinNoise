package me.abdulhamit.terraingenerator;

import de.articdive.jnoise.generators.noise_parameters.fade_functions.FadeFunction;
import de.articdive.jnoise.generators.noise_parameters.interpolation.Interpolation;
import de.articdive.jnoise.pipeline.JNoise;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.noise.NoiseGenerator;
import org.bukkit.util.noise.PerlinNoiseGenerator;
import org.jetbrains.annotations.NotNull;

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
            int size = Integer.parseInt(args[2]);
            
            generateChunk((Player) commandSender, smoothness, height, size);
        } catch (Exception e){
            commandSender.sendMessage("Error executing command: "+e.getMessage());
        }
        
        
        
        return true;
    }
    
    
    public static void generateChunk(Player p, double smoothness, double yscale, int size){
        final Location loc = p.getLocation().clone();
        final World world = loc.getWorld();
        
        for (int x = -size; x<size; x++){
            for (int z = -size; z<size; z++){
                int height = (int) Math.round(noisePipeline.evaluateNoise((loc.getX()+x)/smoothness, (loc.getZ()+z)/smoothness)*yscale);
                for (int y = height; y >=-20; y--){
                    new Location(world,loc.getX()+x, y, loc.getZ()+z).getBlock().setType(Material.STONE);
                }
            }
        }
    }
    
    public static void resetSeed(){
        seed = new Random().nextInt(5000);
        noisePipeline = JNoise.
                newBuilder()
                .scale(40.0)
                .perlin(seed, Interpolation.COSINE, FadeFunction.SMOOTHSTEP)
                .build();
    }
}
