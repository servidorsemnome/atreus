package com.doceazedo.atreus.gatekeeper

import com.doceazedo.atreus.Atreus
import com.doceazedo.atreus.utils.isDateExpired
import com.doceazedo.atreus.utils.sendPlayer
import kotlinx.coroutines.delay
import org.bukkit.entity.Player

val gkUrl: String = Atreus.instance!!.config.getString("gatekeeper.baseURL")
const val hr = "§2======================================="
const val flowChecks = 60 / 2
const val checkDelay: Long = 2000

object Gatekeeper {
    suspend fun startLoginFlow (player: Player) {
        val nickname = player.displayName
        val ip = player.address.address.hostAddress

        // TODO: Check if nickname is registered

        val shouldContinueFlow = handleGrant(player, nickname, ip)
        if (!shouldContinueFlow) return

        val flow = GatekeeperClient.createFlow(FlowRequest(nickname, ip))
                ?: return player.kickPlayer("§cNão foi possível iniciar fluxo de login")

        player.sendMessage(hr)
        player.sendMessage(" ")
        player.sendMessage("§aOi, §e${player.displayName}§a!")
        player.sendMessage("§aClique no link abaixo para fazer login:")
        player.sendMessage("§e§l§n${gkUrl}/${flow.code}")
        player.sendMessage(" ")
        player.sendMessage(hr)

        for (i in 1..flowChecks) {
            Atreus.instance!!.logger.info("Checking flow ${flow.code} for $nickname ($i/$flowChecks)")
            val shouldCheckAgain = checkLoginFlow(player, nickname, ip, flow.code)
            if (!shouldCheckAgain || i == flowChecks) {
                player.kickPlayer("§cSeu tempo acabou.")
                break
            }
            delay(checkDelay)
        }
    }

    private suspend fun handleGrant(player: Player, nickname: String, ip: String): Boolean {
        val grant = GatekeeperClient.getGrant(nickname, ip)
        if ((grant != null) && grant.authorized && !isDateExpired(grant.expirationDate)) {
            Atreus.instance!!.logger.info("Sending player...")
            Atreus.instance!!.logger.info("Data de expiração: ${grant.expirationDate}")
            Atreus.instance!!.logger.info(if (isDateExpired(grant.expirationDate)) "expirado" else "válido")
            sendPlayer(player)
            return false
        }
        if (grant != null && !isDateExpired(grant.expirationDate)) {
            player.kickPlayer("§cIP não autorizado")
            return false
        }
        return true
    }

    private suspend fun checkLoginFlow(player: Player, nickname: String, ip: String, code: String): Boolean {
        if (!player.isOnline) return false
        val flow = GatekeeperClient.getFlow(code)
        if (flow == null || isDateExpired(flow.expirationDate)) {
            player.kickPlayer("§cSeu tempo acabou.")
            return false
        }
        if (flow.grantId != null) return handleGrant(player, nickname, ip)
        return true
    }
}