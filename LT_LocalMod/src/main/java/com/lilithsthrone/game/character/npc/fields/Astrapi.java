package com.lilithsthrone.game.character.npc.fields;

import java.time.Month;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.lilithsthrone.game.character.CharacterImportSetting;
import com.lilithsthrone.game.character.EquipClothingSetting;
import com.lilithsthrone.game.character.body.coverings.BodyCoveringType;
import com.lilithsthrone.game.character.body.coverings.Covering;
import com.lilithsthrone.game.character.body.valueEnums.AreolaeSize;
import com.lilithsthrone.game.character.body.valueEnums.AssSize;
import com.lilithsthrone.game.character.body.valueEnums.BodyHair;
import com.lilithsthrone.game.character.body.valueEnums.BodySize;
import com.lilithsthrone.game.character.body.valueEnums.BreastShape;
import com.lilithsthrone.game.character.body.valueEnums.Capacity;
import com.lilithsthrone.game.character.body.valueEnums.ClitorisSize;
import com.lilithsthrone.game.character.body.valueEnums.CupSize;
import com.lilithsthrone.game.character.body.valueEnums.HairLength;
import com.lilithsthrone.game.character.body.valueEnums.HairStyle;
import com.lilithsthrone.game.character.body.valueEnums.HipSize;
import com.lilithsthrone.game.character.body.valueEnums.LabiaSize;
import com.lilithsthrone.game.character.body.valueEnums.LipSize;
import com.lilithsthrone.game.character.body.valueEnums.Muscle;
import com.lilithsthrone.game.character.body.valueEnums.NippleSize;
import com.lilithsthrone.game.character.body.valueEnums.OrificeElasticity;
import com.lilithsthrone.game.character.body.valueEnums.OrificePlasticity;
import com.lilithsthrone.game.character.body.valueEnums.TongueLength;
import com.lilithsthrone.game.character.body.valueEnums.Wetness;
import com.lilithsthrone.game.character.effects.Perk;
import com.lilithsthrone.game.character.effects.PerkCategory;
import com.lilithsthrone.game.character.effects.PerkManager;
import com.lilithsthrone.game.character.fetishes.Fetish;
import com.lilithsthrone.game.character.fetishes.FetishDesire;
import com.lilithsthrone.game.character.gender.Gender;
import com.lilithsthrone.game.character.npc.NPC;
import com.lilithsthrone.game.character.persona.NameTriplet;
import com.lilithsthrone.game.character.persona.Occupation;
import com.lilithsthrone.game.character.persona.PersonalityTrait;
import com.lilithsthrone.game.character.persona.SexualOrientation;
import com.lilithsthrone.game.character.race.RaceStage;
import com.lilithsthrone.game.character.race.Subspecies;
import com.lilithsthrone.game.combat.DamageType;
import com.lilithsthrone.game.dialogue.DialogueNode;
import com.lilithsthrone.game.inventory.CharacterInventory;
import com.lilithsthrone.game.inventory.InventorySlot;
import com.lilithsthrone.game.inventory.clothing.AbstractClothing;
import com.lilithsthrone.game.inventory.clothing.ClothingType;
import com.lilithsthrone.main.Main;
import com.lilithsthrone.utils.Util;
import com.lilithsthrone.utils.Util.Value;
import com.lilithsthrone.utils.colours.PresetColour;
import com.lilithsthrone.world.WorldType;
import com.lilithsthrone.world.places.PlaceType;

/**
 * @since 0.4
 * @version 0.4
 * @author Innoxia
 */
public class Astrapi extends NPC {

	public Astrapi() {
		this(false);
	}
	
	public Astrapi(boolean isImported) {
		super(isImported, new NameTriplet("Astrapi"), "Grigori",
				"The younger sister of the only two centaurs brave enough to keep their Dominion-to-Elis transport business running, Astrapi has the body of a Greek goddess, and a fiery, indomitable personality to match.",
				29, Month.AUGUST, 15,
				20, Gender.F_V_B_FEMALE, Subspecies.CENTAUR, RaceStage.PARTIAL_FULL,
				new CharacterInventory(10),
				WorldType.EMPTY, PlaceType.GENERIC_HOLDING_CELL,
				true);
		
		if(!isImported) {
		}
	}

	@Override
	public void loadFromXML(Element parentElement, Document doc, CharacterImportSetting... settings) {
		loadNPCVariablesFromXML(this, null, parentElement, doc, settings);
	}

	@Override
	public void setupPerks(boolean autoSelectPerks) {
		this.addSpecialPerk(Perk.SPECIAL_HEALTH_FANATIC);
		this.addSpecialPerk(Perk.SPECIAL_RANGED_EXPERT);
		
		PerkManager.initialisePerks(this,
				Util.newArrayListOfValues(Perk.RANGED_DAMAGE),
				Util.newHashMapOfValues(
						new Value<>(PerkCategory.PHYSICAL, 1),
						new Value<>(PerkCategory.LUST, 0),
						new Value<>(PerkCategory.ARCANE, 0)));
	}
	
	@Override
	public void setStartingBody(boolean setPersona) {
		// Persona:
		if(setPersona) {
			this.setPersonalityTraits(
					PersonalityTrait.CONFIDENT,
					PersonalityTrait.BRAVE,
					PersonalityTrait.INNOCENT);
			
			this.setSexualOrientation(SexualOrientation.AMBIPHILIC);
			
			this.setHistory(Occupation.NPC_TAUR_TRANSPORT);

			this.addFetish(Fetish.FETISH_EXHIBITIONIST);
			this.addFetish(Fetish.FETISH_CUM_ADDICT);
			
			this.setFetishDesire(Fetish.FETISH_DENIAL_SELF, FetishDesire.THREE_LIKE);
		}
		
		
		// Body:
		
		// Core:
		this.setHeight(200);
		this.setFemininity(65);
		this.setMuscle(Muscle.FOUR_RIPPED.getMedianValue());
		this.setBodySize(BodySize.THREE_LARGE.getMedianValue());
		
		// Coverings:
		this.setEyeCovering(new Covering(BodyCoveringType.EYE_HORSE_MORPH, PresetColour.EYE_AMBER));
		this.setSkinCovering(new Covering(BodyCoveringType.HUMAN, PresetColour.SKIN_OLIVE), true);
		this.setSkinCovering(new Covering(BodyCoveringType.HORSE_HAIR, PresetColour.COVERING_AUBURN), true);

		this.setSkinCovering(new Covering(BodyCoveringType.ANUS, PresetColour.SKIN_DARK), false);
		this.setSkinCovering(new Covering(BodyCoveringType.VAGINA, PresetColour.SKIN_DARK), false);
		
		this.setHairCovering(new Covering(BodyCoveringType.HAIR_HUMAN, PresetColour.COVERING_SANDY), true);
		this.setHairCovering(new Covering(BodyCoveringType.HAIR_HORSE_HAIR, PresetColour.COVERING_SANDY), true);
		this.setHairLength(HairLength.THREE_SHOULDER_LENGTH.getMedianValue());
		this.setHairStyle(HairStyle.WAVY);
		
		this.setHairCovering(new Covering(BodyCoveringType.BODY_HAIR_HORSE_HAIR, PresetColour.COVERING_DIRTY_BLONDE), false);
		this.setHairCovering(new Covering(BodyCoveringType.BODY_HAIR_HUMAN, PresetColour.COVERING_DIRTY_BLONDE), false);
		this.setUnderarmHair(BodyHair.ZERO_NONE);
		this.setAssHair(BodyHair.ZERO_NONE);
		this.setPubicHair(BodyHair.ZERO_NONE);
		this.setFacialHair(BodyHair.ZERO_NONE);

//		this.setFootNailPolish(new Covering(BodyCoveringType.MAKEUP_NAIL_POLISH_FEET, PresetColour.COVERING_PURPLE));
//		this.setHandNailPolish(new Covering(BodyCoveringType.MAKEUP_NAIL_POLISH_HANDS, PresetColour.COVERING_PURPLE));
//		this.setBlusher(new Covering(BodyCoveringType.MAKEUP_BLUSHER, PresetColour.COVERING_BLACK));
//		this.setLipstick(new Covering(BodyCoveringType.MAKEUP_LIPSTICK, PresetColour.COVERING_PURPLE));
//		this.setEyeLiner(new Covering(BodyCoveringType.MAKEUP_EYE_LINER, PresetColour.COVERING_BLACK));
//		this.setEyeShadow(new Covering(BodyCoveringType.MAKEUP_EYE_SHADOW, PresetColour.COVERING_PURPLE));
		
		// Face:
		this.setFaceVirgin(false);
		this.setLipSize(LipSize.TWO_FULL);
		this.setFaceCapacity(Capacity.FIVE_ROOMY, true);
		// Throat settings and modifiers
		this.setTongueLength(TongueLength.ZERO_NORMAL.getMedianValue());
		// Tongue modifiers
		
		// Chest:
		this.setNippleVirgin(true);
		this.setBreastSize(CupSize.D.getMeasurement());
		this.setBreastShape(BreastShape.PERKY);
		this.setNippleSize(NippleSize.THREE_LARGE);
		this.setAreolaeSize(AreolaeSize.THREE_LARGE);
		// Nipple settings and modifiers
		
		// Ass:
		this.setAssVirgin(true);
		this.setAssBleached(false);
		this.setAssSize(AssSize.THREE_NORMAL);
		this.setHipSize(HipSize.THREE_GIRLY);
		this.setAssCapacity(Capacity.TWO_TIGHT, true);
		this.setAssWetness(Wetness.ZERO_DRY);
		// Horse-like modifiers:
//		this.addAssOrificeModifier(OrificeModifier.PUFFY);
//		this.addAssOrificeModifier(OrificeModifier.MUSCLE_CONTROL);
		
		// Penis:
//		this.setPenisVirgin(false);
//		this.setPenisGirth(PenetrationGirth.FOUR_GIRTHY);
//		this.setPenisSize(46);
//		this.setTesticleSize(TesticleSize.FOUR_HUGE);
//		this.setPenisCumStorage(500);
//		this.setPenisCumExpulsion(85);
//		this.fillCumToMaxStorage();
//		this.setTesticleCount(2);
//		// Horse-like modifiers:
//		this.clearPenisModifiers();
//		this.addPenisModifier(PenetrationModifier.FLARED);
//		this.addPenisModifier(PenetrationModifier.VEINY);
//		this.addPenisModifier(PenetrationModifier.SHEATHED);
//		this.addCumModifier(FluidModifier.MUSKY);
		
		// Vagina:
		this.setVaginaVirgin(false);
		this.setVaginaClitorisSize(ClitorisSize.ZERO_AVERAGE);
		this.setVaginaLabiaSize(LabiaSize.TWO_AVERAGE);
		this.setVaginaSquirter(false);
		this.setVaginaCapacity(Capacity.THREE_SLIGHTLY_LOOSE, true);
		this.setVaginaWetness(Wetness.TWO_MOIST);
		this.setVaginaElasticity(OrificeElasticity.TWO_FIRM.getValue());
		this.setVaginaPlasticity(OrificePlasticity.FOUR_ACCOMMODATING.getValue());
		
		// Feet:
		// Foot shape
	}
	
	@Override
	public void equipClothing(List<EquipClothingSetting> settings) {
		this.clearNonEquippedInventory(false);
		this.unequipAllClothingIntoVoid(true, true);

		this.equipClothingFromNowhere(Main.game.getItemGen().generateClothing("innoxia_head_sweatband", PresetColour.CLOTHING_BLUE_GREY, false), true, this);
		this.equipClothingFromNowhere(Main.game.getItemGen().generateClothing(ClothingType.WRIST_WRISTBANDS, PresetColour.CLOTHING_BLUE_GREY, false), true, this);
		
		AbstractClothing bra = Main.game.getItemGen().generateClothing(ClothingType.CHEST_SPORTS_BRA, PresetColour.CLOTHING_BLACK, false);
		this.addClothing(bra, false);
		
		this.setPiercedEar(true);
//		this.equipClothingFromNowhere(Main.game.getItemGen().generateClothing("innoxia_piercing_ear_ball_studs", PresetColour.CLOTHING_SILVER, false), true, this);
		
		this.setEssenceCount(100);
		this.equipMainWeaponFromNowhere(Main.game.getItemGen().generateWeapon("innoxia_bow_shortbow", DamageType.FIRE));
	}
	
	public void applySportsBra(boolean equip) {
		if(equip) {
			AbstractClothing bra = null;
			for(AbstractClothing c : this.getAllClothingInInventory().keySet()) {
				if(c.getClothingType()==ClothingType.CHEST_SPORTS_BRA) {
					bra = c;
					break;
				}
			}
			if(bra!=null) {
				this.equipClothingFromInventory(bra, true, this, this);
			} else {
				this.equipClothingFromNowhere(Main.game.getItemGen().generateClothing(ClothingType.CHEST_SPORTS_BRA, PresetColour.CLOTHING_BLACK, false), true, this);
			}
			
		} else {
			AbstractClothing bra = this.getClothingInSlot(InventorySlot.CHEST);
			if(bra!=null) {
				this.unequipClothingIntoInventory(bra, true, this);
			}
		}
	}
	
	@Override
	public boolean isUnique() {
		return true;
	}
	
	@Override
	public String getSpeechColour() {
		if(Main.game.isLightTheme()) {
			return "#918365";
		}
		return "#f4dcaa";
	}
	
	@Override
	public void changeFurryLevel(){
	}
	
	@Override
	public DialogueNode getEncounterDialogue() {
		return null;
	}
	
	@Override
	public void dailyUpdate() {
	}
	
	@Override
	public void endSex() {
	}
	
	@Override
	public boolean isAbleToBeImpregnated() {
		return true;
	}

}