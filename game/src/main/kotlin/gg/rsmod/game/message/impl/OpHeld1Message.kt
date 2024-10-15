package gg.rsmod.game.message.impl

import gg.rsmod.game.message.Message

/**
 * @author Tom <rspsmods@gmail.com>
 */
data class OpHeld1Message(
    val item: Int,
    val slot: Int,
    val componentHash: Int,
) : Message
