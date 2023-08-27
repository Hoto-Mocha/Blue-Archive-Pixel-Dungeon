package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;
import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Fire;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Web;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Charm;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Poison;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.Beam;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.BlastParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.PoisonParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.PurpleParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SmokeParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.SpiritBow;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class SuperNova extends MeleeWeapon {

    {
        image = ItemSpriteSheet.SUPER_NOVA;

        defaultAction = AC_SHOOT;
        usesTargeting = false;

        tier = 1;

        bones = false;
        unique = true;
        levelKnown = true;
    }

    private static final String AC_SHOOT = "SHOOT";

    @Override
    public int max(int lvl) {
        int lvlTier = tier + hero.lvl/6;
        return  4*(lvlTier+1) +
                lvl*(lvlTier+1);
    }

    @Override
    public ArrayList<String> actions(Hero hero ) {
        ArrayList<String> actions = super.actions( hero );
        actions.remove( AC_EQUIP );
        if (hero.hasTalent(Talent.HOLYSWORD) && !isEquipped(hero)) {
            actions.add( AC_EQUIP );
        }
        actions.add( AC_SHOOT );
        return actions;
    }

    @Override
    public int buffedLvl() {
        int lvl = super.buffedLvl();

        if (hero.pointsInTalent(Talent.HOLYSWORD) >= 3) {
            lvl += Math.floor(hero.lvl/5f);
        }
        return lvl;
    }

    @Override
    public void execute( Hero hero, String action ) {

        super.execute( hero, action );

        if (action.equals(AC_SHOOT)) {
            if (hero.buff(SuperNovaCooldown.class) != null) {
                usesTargeting = false;
                hero.yellW(Messages.get(Hero.class, "aris_supernova_cooldown"));
            } else {
                usesTargeting = true;
                curUser = hero;
                curItem = this;
                GameScene.selectCell(shooter);
            }
        }
    }

    public int beamDamageMin(int lvl) {
        float damage = hero.lvl + Dungeon.depth + lvl;
        if (hero.hasTalent(Talent.BALANCE_COLLAPSE)) {
            damage *= 1 + 0.05f * hero.pointsInTalent(Talent.BALANCE_COLLAPSE);
        }
        if (hero.hasTalent(Talent.OVERCHARGE) && hero.buff(SuperNovaCharge.class) != null) {
            damage *= 1+0.05f*hero.pointsInTalent(Talent.OVERCHARGE)*hero.buff(SuperNovaCharge.class).chargeLevel;
        }
        return Math.round(damage);
    }

    public int beamDamageMax(int lvl) {
        float damage = hero.lvl*3 + Dungeon.depth*2 + lvl*3;
        if (hero.hasTalent(Talent.BALANCE_COLLAPSE)) {
            damage *= 1+0.05f*hero.pointsInTalent(Talent.BALANCE_COLLAPSE);
        }
        if (hero.hasTalent(Talent.OVERCHARGE) && hero.buff(SuperNovaCharge.class) != null) {
            damage *= 1+0.05f*hero.pointsInTalent(Talent.OVERCHARGE)*hero.buff(SuperNovaCharge.class).chargeLevel;
        }
        return Math.round(damage);
    }

    public int maxDistance() {
        float dist = 8;
        if (hero.buff(SuperNovaCharge.class) != null && hero.buff(SuperNovaCharge.class).chargeLevel() > 2) dist *= 1.5f;
        if (hero.hasTalent(Talent.INCREASING_OUTPUT)) dist *= (1+0.25f*hero.pointsInTalent(Talent.INCREASING_OUTPUT));
        return (int)dist;
    }

    public float coolDown() {
        float coolDown = 100;
        if (hero.hasTalent(Talent.ACCEL_ENERGY)) {
            coolDown *= (1-0.1f*hero.pointsInTalent(Talent.ACCEL_ENERGY));
        }
        if (hero.buff(SuperNovaCharge.class) != null && hero.hasTalent(Talent.CHARGE_ACCEL)) {
            coolDown *= (1-0.05f*hero.pointsInTalent(Talent.CHARGE_ACCEL)*hero.buff(SuperNovaCharge.class).chargeLevel());
        }
        return coolDown;
    }

    private CellSelector.Listener shooter = new CellSelector.Listener() {
        @Override
        public void onSelect( Integer target ) {
            if (target != null) {
                if (target == curUser.pos) {
                    hero.yellW(Messages.get(Hero.class, "aris_cannot_self"));

                } else {

                    boolean terrainAffected = false;
                    int maxDistance = maxDistance();

                    if (hero.subClass == HeroSubClass.ARIS_EX_SUPERNOVA) {
                        maxDistance = Dungeon.level.distance(hero.pos, target);
                    }

                    Ballistica beam = new Ballistica(curUser.pos, target, Ballistica.WONT_STOP);
                    ArrayList<Char> chars = new ArrayList<>();

                    for (int c : beam.subPath(1, maxDistance)) {
                        Char ch;

                        //각성 능력: 과충전 2단계
                        if (hero.buff(SuperNovaCharge.class) != null && hero.buff(SuperNovaCharge.class).chargeLevel() > 1) {
                            for (int i : PathFinder.NEIGHBOURS9) {
                                int cell = c + i;
                                if ((ch = Actor.findChar( cell )) != null) {
                                    if ((ch instanceof Mob && ((Mob) ch).state == ((Mob) ch).PASSIVE
                                            && !(Dungeon.level.mapped[cell] || Dungeon.level.visited[cell])) || (ch instanceof Hero) || chars.contains(ch)){
                                        //avoid harming undiscovered passive chars
                                    } else {
                                        chars.add(ch);
                                    }
                                }
                                CellEmitter.get(cell).burst(SmokeParticle.FACTORY, 2);
                                CellEmitter.center(cell).burst(BlastParticle.FACTORY, 2);
                                if (Dungeon.level.flamable[cell]) {
                                    Dungeon.level.destroy( cell );
                                    GameScene.updateMap( cell );
                                    terrainAffected = true;
                                }
                            }
                        } else {
                            if ((ch = Actor.findChar( c )) != null) {
                                if ((ch instanceof Mob && ((Mob) ch).state == ((Mob) ch).PASSIVE
                                        && !(Dungeon.level.mapped[c] || Dungeon.level.visited[c])) || (ch instanceof Hero)){
                                    //avoid harming undiscovered passive chars
                                } else {
                                    chars.add(ch);
                                }
                            }
                            if (Dungeon.level.flamable[c]) {
                                Dungeon.level.destroy( c );
                                GameScene.updateMap( c );
                                terrainAffected = true;
                            }
                        }

                        CellEmitter.center( c ).burst( PurpleParticle.BURST, Random.IntRange( 1, 2 ) );

                        if (terrainAffected) {
                            Dungeon.observe();
                        }

                    }
                    for (Char ch : chars) {

                        //각성 능력: 과충전 1단계
                        if (hero.buff(SuperNovaCharge.class) != null) {
                            if (hero.buff(SuperNovaCharge.class).chargeLevel() > 0) {
                                Buff.affect(ch, Burning.class).reignite(ch);
                            }
                        }

                        ch.damage( Random.NormalIntRange(beamDamageMin(buffedLvl()), beamDamageMax(buffedLvl())), this );
                        ch.sprite.centerEmitter().burst( PurpleParticle.BURST, Random.IntRange( 1, 2 ) );
                        ch.sprite.flash();
                    }

                    if (hero.hasTalent(Talent.ENERGY_DRAIN)) {
                        hero.heal(chars.size() * hero.pointsInTalent(Talent.ENERGY_DRAIN));
                    }

                    //각성 능력: 슈퍼노바
                    if (hero.subClass == HeroSubClass.ARIS_EX_SUPERNOVA) {
                        boolean terrainAffected2 = false;
                        ArrayList<Char> charsBlastAffected = new ArrayList<>();
                        for (int i : PathFinder.NEIGHBOURS25) {
                            int cell = target+i;
                            Char enemy;
                            if ((enemy = Actor.findChar(cell)) != null && enemy.alignment == Char.Alignment.ENEMY) {
                                charsBlastAffected.add(enemy);
                            }

                            if (Dungeon.level.flamable[cell]) {
                                Dungeon.level.destroy( cell );
                                GameScene.updateMap( cell );
                                terrainAffected2 = true;
                            }
                            CellEmitter.get(cell).burst(SmokeParticle.FACTORY, 4);
                            if (Dungeon.level.map[target] != Terrain.WALL && Dungeon.level.map[target] != Terrain.WALL_DECO) {
                                GameScene.add( Blob.seed( cell, 3, Fire.class ) );
                            }
                        }
                        if (terrainAffected2) {
                            Dungeon.observe();
                        }
                        if (Dungeon.level.map[target] != Terrain.SOLID) {
                            CellEmitter.center(target).burst(BlastParticle.FACTORY, 200);
                        }
                        for (Char victim : charsBlastAffected) {
                            victim.damage( Random.NormalIntRange(beamDamageMin(buffedLvl()), beamDamageMax(buffedLvl())), this );
                        }
                    }

                    curUser.sprite.zap(target);
                    int cell = beam.path.get(Math.min(beam.dist, maxDistance));
                    curUser.sprite.parent.add(new Beam.DeathRay(curUser.sprite.center(), DungeonTilemap.raisedTileCenterToWorld( cell )));
                    Dungeon.hero.yellI(Messages.get(Hero.class, "aris_supernova_" + (Random.Int(3)+1)));

                    Buff.affect(hero, SuperNovaCooldown.class).set(coolDown());

                    if (hero.buff(SuperNovaCharge.class) != null) {
                        hero.buff(SuperNovaCharge.class).detach();
                    }

                    if(hero.hasTalent(Talent.EMPOWERING_MAGIC)) {
                        Buff.affect(hero, Talent.EmpoweringMagic.class, 5);
                    }

                    hero.spendAndNext(Actor.TICK);
                }
            }
        }
        @Override
        public String prompt() {
            return Messages.get(SpiritBow.class, "prompt");
        }
    };

    @Override
    public boolean isUpgradable() {
        if (hero.pointsInTalent(Talent.HOLYSWORD) >= 2) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean isIdentified() {
        return true;
    }

    @Override
    public int value() {
        return -1;
    }

    @Override
    public String info() {

        String info = desc();

        if( hero.hasTalent(Talent.HOLYSWORD) ) {
            if (levelKnown) {
                info += "\n\n" + Messages.get(MeleeWeapon.class, "stats_known", tier, augment.damageFactor(min()), augment.damageFactor(max()), STRReq());
                if (STRReq() > Dungeon.hero.STR()) {
                    info += " " + Messages.get(Weapon.class, "too_heavy");
                } else if (Dungeon.hero.STR() > STRReq()){
                    info += " " + Messages.get(Weapon.class, "excess_str", Dungeon.hero.STR() - STRReq());
                }
            } else {
                info += "\n\n" + Messages.get(MeleeWeapon.class, "stats_unknown", tier, min(0), max(0), STRReq(0));
                if (STRReq(0) > Dungeon.hero.STR()) {
                    info += " " + Messages.get(MeleeWeapon.class, "probably_too_heavy");
                }
            }

            String statsInfo = statsInfo();
            if (!statsInfo.equals("")) info += "\n\n" + statsInfo;

            switch (augment) {
                case SPEED:
                    info += " " + Messages.get(Weapon.class, "faster");
                    break;
                case DAMAGE:
                    info += " " + Messages.get(Weapon.class, "stronger");
                    break;
                case NONE:
            }

            if (enchantment != null && (cursedKnown || !enchantment.curse())){
                info += "\n\n" + Messages.capitalize(Messages.get(Weapon.class, "enchanted", enchantment.name()));
                info += " " + enchantment.desc();
            }

            if (cursed && isEquipped( Dungeon.hero )) {
                info += "\n\n" + Messages.get(Weapon.class, "cursed_worn");
            } else if (cursedKnown && cursed) {
                info += "\n\n" + Messages.get(Weapon.class, "cursed");
            } else if (!isIdentified() && cursedKnown){
                if (enchantment != null && enchantment.curse()) {
                    info += "\n\n" + Messages.get(Weapon.class, "weak_cursed");
                } else {
                    info += "\n\n" + Messages.get(Weapon.class, "not_cursed");
                }
            }
        }

        return info;
    }

    @Override
    public String desc() {
        return Messages.get(this, "desc", beamDamageMin(buffedLvl()), beamDamageMax(buffedLvl()));
    }

    public static class SuperNovaCharge extends Buff {

        private float turnsToCharge = 50;
        private float time = 0;
        private int chargeLevel = 0;

        {
            type = buffType.NEUTRAL;
        }

        public int chargeLevel() {
            return chargeLevel;
        }

        @Override
        public boolean act() {
            time+=TICK;
            spend(TICK);
            if (chargeLevel < 3) {
                if (time >= turnsToCharge) {
                    chargeLevel++;
                    Dungeon.hero.yellP(Messages.get(Hero.class, hero.heroClass.name() + "_supernova_charge_" + chargeLevel));
                    time = 0;
                }
            } else {
                time = turnsToCharge;
            }
            BuffIndicator.refreshHero();
            return true;
        }

        @Override
        public int icon() {
            return BuffIndicator.RECHARGING;
        }

        @Override
        public void tintIcon(Image icon) {
            switch (chargeLevel) {
                case 0: default:
                    icon.hardlight(1, 1, 1);
                    break;
                case 1:
                    icon.hardlight(0, 1, 0);
                    break;
                case 2:
                    icon.hardlight(1, 1, 0);
                    break;
                case 3:
                    icon.hardlight(1, 0, 0);
                    break;
            }
        }

        @Override
        public float iconFadePercent() {
            return Math.max(0, time / turnsToCharge);
        }

        private static final String TIME  = "time";
        private static final String TURNSTOCHARGE  = "turnsToCharge";
        private static final String CHARGELEVEL  = "chargeLevel";

        @Override
        public void storeInBundle(Bundle bundle) {
            super.storeInBundle(bundle);
            bundle.put(TIME, time);
            bundle.put(TURNSTOCHARGE, turnsToCharge);
            bundle.put(CHARGELEVEL, chargeLevel);
        }

        @Override
        public void restoreFromBundle(Bundle bundle) {
            super.restoreFromBundle(bundle);
            time = bundle.getFloat( TIME );
            turnsToCharge = bundle.getFloat( TURNSTOCHARGE );
            chargeLevel = bundle.getInt( CHARGELEVEL );
        }

        @Override
        public String desc() {
            String desc = Messages.get(this, "desc_1", chargeLevel);

            for (int i = 1; i <= chargeLevel; i++) {
                desc += Messages.get(this, "charge_" + i);
            }

            desc += Messages.get(this, "desc_2", Messages.decimalFormat("#.##", time), Messages.decimalFormat("#.##", turnsToCharge));

            return desc;
        }

        @Override
        public String iconTextDisplay() {
            return Messages.decimalFormat("#.##", time/turnsToCharge*100) + "%";
        }

//        @Override
//        public void detach() {
//            super.detach();
//        }

    }

    public static class SuperNovaCooldown extends Buff {

        float maxDuration;
        float duration;

        {
            type = buffType.NEUTRAL;
        }

        public void set(float time) {
            maxDuration = time;
            duration = maxDuration;
        }

        public void hit() {
            duration -= hero.pointsInTalent(Talent.PIEZOELECTRICITY);
            if (duration <= 0) {
                detach();
            }
            BuffIndicator.refreshHero();
        }

        @Override
        public boolean act() {
            duration -= TICK;
            spend(TICK);
            if (duration <= 0) {
                detach();
            }
            BuffIndicator.refreshHero();
            return true;
        }

        @Override
        public int icon() {
            return BuffIndicator.TIME;
        }

        @Override
        public float iconFadePercent() {
            return Math.max(0, (maxDuration - duration) / maxDuration);
        }

        private static final String DURATION  = "duration";
        private static final String MAX_DURATION  = "maxDuration";

        @Override
        public void storeInBundle(Bundle bundle) {
            super.storeInBundle(bundle);
            bundle.put(DURATION, duration);
            bundle.put(MAX_DURATION, maxDuration);
        }

        @Override
        public void restoreFromBundle(Bundle bundle) {
            super.restoreFromBundle(bundle);
            duration = bundle.getFloat( DURATION );
            maxDuration = bundle.getFloat( MAX_DURATION );
        }

        @Override
        public String desc() {
            return Messages.get(this, "desc", Messages.decimalFormat("#.##", duration));
        }

        @Override
        public void detach() {
            Dungeon.hero.yellP(Messages.get(Hero.class, hero.heroClass.name() + "_supernova_ready_" + (Random.Int(3)+1)));
            if (hero.subClass == HeroSubClass.ARIS_EX_CHARGE) {
                Buff.affect(hero, SuperNovaCharge.class);
            }
            super.detach();
        }

        @Override
        public String iconTextDisplay() {
            return Integer.toString((int)duration);
        }
    }
}
