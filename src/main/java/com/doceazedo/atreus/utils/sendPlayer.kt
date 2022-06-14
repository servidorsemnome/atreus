package com.doceazedo.atreus.utils

import com.doceazedo.atreus.Atreus
import com.google.common.io.ByteStreams
import org.bukkit.entity.Player

val targetServer: String = Atreus.instance!!.config.getString("targetServer")

fun sendPlayer (player: Player, server: String = targetServer) {
    try {
        val out = ByteStreams.newDataOutput()
        out.writeUTF("Connect")
        out.writeUTF(server)
        player.sendPluginMessage(Atreus.instance, "BungeeCord", out.toByteArray())
    } catch (cause: Throwable) {
        cause.printStackTrace()
        player.kickPlayer("§cNão foi possível te redirecionar para outro servidor")
    }
}