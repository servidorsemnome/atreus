package com.doceazedo.atreus

import com.doceazedo.atreus.events.*
import com.doceazedo.atreus.gatekeeper.GatekeeperClient
import com.github.shynixn.mccoroutine.bukkit.registerSuspendingEvents
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

class Atreus : JavaPlugin() {
    companion object {
        var instance: Atreus? = null
    }

    override fun onEnable() {
        instance = this
        instance?.saveDefaultConfig()
        Bukkit.getPluginManager().registerSuspendingEvents(PlayerJoin, this)
        Bukkit.getPluginManager().registerEvents(EntityDamage, this)
        Bukkit.getPluginManager().registerEvents(FoodLevelChange, this)
        Bukkit.getPluginManager().registerEvents(PlayerInteract, this)
        Bukkit.getPluginManager().registerEvents(PlayerMove, this)
    }

    override fun onDisable() {
        GatekeeperClient.close()
    }
}