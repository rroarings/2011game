package gg.rsmod.plugins.content.combat.specialattack.weapons.saradominsword

import gg.rsmod.plugins.content.combat.dealHit
import gg.rsmod.plugins.content.combat.formula.MeleeCombatFormula
import gg.rsmod.plugins.content.combat.specialattack.SpecialAttacks

val SPECIAL_REQUIREMENT = 100
val SARASWORD_SPEC_ANIMATION = 11993
val SARASWORD_SPEC_PLAYER_GFX = 2115
val SARASWORD_SPEC_TARGET_GFX = 1194
val MAGIC_DAMAGE_MAX_HIT = 20.0
val MAGIC_DAMAGE_MIN_HIT = 5.0
val SARASWORD_SPEC_SFX_ID1 = 3887 // According to https://www.runelister.com/forum/topic/osrs-sound-effect-list/
val SARASWORD_SPEC_SFX_ID2 = 3869
/**
 * From the wiki in 2011 (see https://runescape.wiki/w/Saradomin_sword?oldid=4832090):
 * Its special attack, Saradomin's Lightning, adds 50-200 extra magic-based melee damage to the standard melee attack
 * and raises the maximum inflicted damage by 10% for the melee hit. Players receive magic experience for using this
 * special, but only for the magical hit. Essentially, the special deals two attacks: the melee attack that the
 * player would normally have hit (with its maximum inflicted damage raised by 10% however) without the special,
 * and an extra magical attack that can deal 50-200(or 90-240 with a ferocious ring) damage. The bonus from a
 * hexcrest or full slayer helm on a slayer task is applied to this magical attack. The special attack drains 100% of
 * the special attack bar.
 */

SpecialAttacks.register(SPECIAL_REQUIREMENT, Items.SARADOMIN_SWORD) {
    //First normal attack
    val maxHit = MeleeCombatFormula.getMaxHit(player, target, specialAttackMultiplier = 1.10)
    val accuracy = MeleeCombatFormula.getAccuracy(player, target)
    val landHit = accuracy >= world.randomDouble()
    val delay = 1
    player.dealHit(
        target = target,
        maxHit = maxHit,
        landHit = landHit,
        delay = delay,
        hitType = HitType.MELEE
    )

    //Magic special attack
    world.spawn(AreaSound(tile = player.tile, id = SARASWORD_SPEC_SFX_ID1, radius = 10, volume = 1))
    world.spawn(AreaSound(tile = player.tile, id = SARASWORD_SPEC_SFX_ID2, radius = 10, volume = 1))

    player.animate(id = SARASWORD_SPEC_ANIMATION)
    player.graphic(SARASWORD_SPEC_PLAYER_GFX)
    target.graphic(SARASWORD_SPEC_TARGET_GFX)

    val totalMagicDamage = player.dealHit(
        target = target,
        maxHit = MAGIC_DAMAGE_MAX_HIT,
        minHit = MAGIC_DAMAGE_MIN_HIT,
        landHit = true,
        delay = 1,
        hitType = HitType.MAGIC
    ).hit.hitmarks.sumOf { x -> x.damage }

    player.addXp(Skills.MAGIC, 0.2*totalMagicDamage, modifiers = false)
}