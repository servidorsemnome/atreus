package com.doceazedo.atreus.events

import com.doceazedo.atreus.Atreus
import com.doceazedo.atreus.gatekeeper.Gatekeeper
import com.github.shynixn.mccoroutine.bukkit.launch
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

object PlayerJoin: Listener {
    @EventHandler
    suspend fun onPlayerJoin(e: PlayerJoinEvent) {
        Atreus.instance!!.launch {
            Gatekeeper.startLoginFlow(e.player)
        }
    }
}