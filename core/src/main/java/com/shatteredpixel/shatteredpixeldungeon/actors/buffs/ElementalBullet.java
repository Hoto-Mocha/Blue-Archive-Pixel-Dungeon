package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;
import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.elementals.APBullet;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.elementals.ElectricBullet;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.elementals.ExplosiveBullet;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.elementals.IceBullet;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.elementals.IncendiaryBullet;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.items.active.Bicycle;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.ActionIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.HeroIcon;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndElementalBullet;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndProfessionalRide;
import com.watabou.utils.Bundle;
public class ElementalBullet extends Buff implements ActionIndicator.Action {

    float duration = 0;

    {
        type = buffType.NEUTRAL;
        announced = false;
    }

    @Override
    public boolean act() {
        ActionIndicator.setAction(ElementalBullet.this);
        spend(TICK);
        duration--;
        if (duration < 0) {
            detach();
        }
        return true;
    }

    public void set(float amount) {
        duration = amount;
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        ActionIndicator.setAction(ElementalBullet.this);
    }

    @Override
    public String toString() {
        return Messages.get(this, "name");
    }

    @Override
    public void detach() {
        super.detach();
        ActionIndicator.clearAction();
    }

    public enum ElementalReload {
        AP_BULLET		    (0xC7C4C9),
        INCENDIARY_BULLET   (0xFFAA33),
        EXPLOSIVE_BULLET    (0xFF2A00),
        ICE_BULLET  	    (0x5674B9),
        ELECTRIC_BULLET	    (0xFFFFFF);

        public int tintColor;

        ElementalReload(int tintColor){
            this.tintColor = tintColor;
        }

        public String title(){
            return Messages.get(this, name() + ".name");
        }

        public String desc(){
            return Messages.get(this, name() + ".desc", hero.pointsInTalent(Talent.ELEMENTAL_BULLET_1));
        }

    }

    public boolean canUseMove(ElementalReload move){
        return true;
    }

    public void useMove(ElementalReload move){
        switch (move) {
            case AP_BULLET:
                hero.yellP(Messages.get(Hero.class, hero.heroClass.name() + "_ap_bullet")); //"철갑탄 장전."
                Buff.affect(hero, APBullet.class);
                break;
            case INCENDIARY_BULLET:
                hero.yellP(Messages.get(Hero.class, hero.heroClass.name() + "_incendiary_bullet")); //"소이탄 장전."
                Buff.affect(hero, IncendiaryBullet.class);
                break;
            case EXPLOSIVE_BULLET:
                hero.yellP(Messages.get(Hero.class, hero.heroClass.name() + "_explosive_bullet"));  //"폭발탄 장전."
                Buff.affect(hero, ExplosiveBullet.class);
                break;
            case ICE_BULLET:
                hero.yellP(Messages.get(Hero.class, hero.heroClass.name() + "_ice_bullet"));    //"빙결탄 장전."
                Buff.affect(hero, IceBullet.class);
                break;
            case ELECTRIC_BULLET:
                hero.yellP(Messages.get(Hero.class, hero.heroClass.name() + "_electric_bullet"));   //"전격탄 장전."
                Buff.affect(hero, ElectricBullet.class);
                break;
        }
        hero.sprite.operate(hero.pos);
        if (hero.pointsInTalent(Talent.ELEMENTAL_BULLET_2) < 3) {
            hero.spendAndNext(Actor.TICK);
        }
        detach();
    }


    //액션인디케이터 내용
    @Override
    public String actionName() {
        return Messages.get(this, "action_name");
    }

    @Override
    public int actionIcon() {
        return HeroIcon.ELEMENTAL_BULLET;
    }

    @Override
    public int indicatorColor() {
        return 0xC7C4C9;
    }

    @Override
    public void doAction() {
        GameScene.show(new WndElementalBullet(this));
    }
}