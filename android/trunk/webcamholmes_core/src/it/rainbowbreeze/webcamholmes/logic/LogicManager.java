/**
 * Copyright (C) 2010 Alfredo Morresi
 * 
 * This file is part of SmsForFree project.
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; If not, see <http://www.gnu.org/licenses/>.
 */
package it.rainbowbreeze.webcamholmes.logic;

import it.rainbowbreeze.libs.R;
import it.rainbowbreeze.libs.common.AppGlobalBag;
import it.rainbowbreeze.libs.common.BaseResultOperation;
import it.rainbowbreeze.libs.log.BaseLogFacility;
import it.rainbowbreeze.libs.logic.BaseLogicManager;
import it.rainbowbreeze.webcamholmes.common.ResultOperation;
import it.rainbowbreeze.webcamholmes.data.AppPreferencesDao;
import it.rainbowbreeze.webcamholmes.data.ItemsDao;
import android.content.Context;
import android.text.TextUtils;

import static it.rainbowbreeze.libs.common.ContractHelper.*;

/**
 * @author Alfredo "Rainbowbreeze" Morresi
 */
public class LogicManager extends BaseLogicManager {

	//---------- Private fields
	private ItemsDao mItemsDao;
	private AppPreferencesDao mAppPreferencesDao;
	
	

	//---------- Constructor
	/**
	 * @param logFacility
	 * @param appPreferencesDao
	 * @param currentAppVersion
	 * @param itemsDao
	 */
	public LogicManager(
			BaseLogFacility logFacility,
			AppPreferencesDao appPreferencesDao,
			AppGlobalBag globalBag,
			String currentAppVersion,
			ItemsDao itemsDao)
	{
		super(logFacility, appPreferencesDao, globalBag, currentAppVersion);
		mItemsDao = checkNotNull(itemsDao);
		mAppPreferencesDao = appPreferencesDao;
	}
	
	
	
	//---------- Public properties
	/* (non-Javadoc)
	 * @see it.rainbowbreeze.libs.logic.BaseLogicManager#executeBeginTask(android.content.Context)
	 */
	@Override
	public BaseResultOperation<Void> executeBeginTask(Context context) {
		super.executeBeginTask(context);
		
		//TODO remove when tests finish
		if (mItemsDao.isDatabaseEmpty()) {
			//test deleted all webcams
			createSystemWebcam(context);
		}
		
		return new ResultOperation<Void>();
	}


	/* (non-Javadoc)
	 * @see it.rainbowbreeze.libs.logic.BaseLogicManager#executeEndTast(android.content.Context)
	 */
	@Override
	public BaseResultOperation<Void> executeEndTast(Context context) {
		BaseResultOperation<Void> res;
		
		res = super.executeEndTast(context);
		if (res.hasErrors()) {
			//TODO
		}
		
		//remove temp resources
		int resourcesRemoved = 0;
		String[] resourcesToRemove = mAppPreferencesDao.getResourcesToRemove();
		for (int i=0; i<resourcesToRemove.length; i++) {
			String resource = resourcesToRemove[i];
			if (context.deleteFile(resource)) {
				resourcesToRemove[i] = "";
				resourcesRemoved++;
			}
		}
		
		//compact resources not removed
		int resourcesToRemoveNewLength = resourcesToRemove.length - resourcesRemoved;
		if (0 == resourcesToRemoveNewLength) {
			mAppPreferencesDao.cleanResourcesToRemove();
		} else {
			String[] newResources = new String[resourcesToRemoveNewLength];
			int index = 0;
			for (String resource:resourcesToRemove) {
				if (!TextUtils.isEmpty(resource)) newResources[index++] = resource;
			}
			mAppPreferencesDao.setResourcesToRemove(newResources);
		}
		
		mAppPreferencesDao.save();
		return res;
	}




	//---------- Public methods

	
	
	
	//---------- Private methods
	/* (non-Javadoc)
	 * @see it.rainbowbreeze.libs.logic.BaseLogicManager#executeUpgradeTasks(java.lang.String)
	 */
	@Override
	protected BaseResultOperation<Void> executeUpgradeTasks(Context context, String startingAppVersion) {
		
		BaseResultOperation<Void> res = createSystemWebcam(context);
		return res;
	}
	

	/**
	 * Creates webcam for version 01.00.00 of the app
	 */
	private BaseResultOperation<Void> createSystemWebcam(Context context) {
//		ItemCategory category;
//		long categoryId;

		mBaseLogFacility.v("Deleting old system webcams");
		mItemsDao.clearDatabaseComplete();

		mBaseLogFacility.v("Adding new system webcams");
		
		//add new item to the list
		ResultOperation<Integer> res = mItemsDao.importFromResource(context, R.xml.items);
		
		if (res.hasErrors()) {
			return new BaseResultOperation<Void>();
		}
		
		return new BaseResultOperation<Void>();
		
//		category = ItemCategory.Factory.getSystemCategory(0, "Traffic - Australia");
//		categoryId = mItemsDao.insertCategory(category);
//		mItemsDao.insertWebcam(ItemWebcam.Factory.getSystemWebcam(categoryId, "Sidney - Anzac Bridge", "http://www.rta.nsw.gov.au/trafficreports/cameras/camera_images/anzacbr.jpg", 20));
//		mItemsDao.insertWebcam(ItemWebcam.Factory.getSystemWebcam(categoryId, "Sidney - William St.", "http://www.rta.nsw.gov.au/trafficreports/cameras/camera_images/williamst.jpg", 20));
//		mItemsDao.insertWebcam(ItemWebcam.Factory.getSystemWebcam(categoryId, "Sidney - Harbour Bridge", "http://www.rta.nsw.gov.au/trafficreports/cameras/camera_images/harbourbridge.jpg", 20));
//		mItemsDao.insertWebcam(ItemWebcam.Factory.getSystemWebcam(categoryId, "Sidney - George St.", "http://www.rta.nsw.gov.au/trafficreports/cameras/camera_images/georgest.jpg", 20));
//		
//		categoryId = mItemsDao.insertCategory(ItemCategory.Factory.getSystemCategory(0, "Traffic - Italy"));
//		mItemsDao.insertWebcam(ItemWebcam.Factory.getSystemWebcam(categoryId, "A4 Torino-Trieste uscita Bergamo", "http://get.edidomus.it/vp/cam1/image.jpg", 5));
//		mItemsDao.insertWebcam(ItemWebcam.Factory.getSystemWebcam(categoryId, "A14 Trezzo sull'Adda", "http://get.edidomus.it/vp/cam23/image.jpg", 5));
//		mItemsDao.insertWebcam(ItemWebcam.Factory.getSystemWebcam(categoryId, "Milano - Tang Est Rubattino", "http://get.edidomus.it/vp/cam4/image.jpg", 5));
//		mItemsDao.insertWebcam(ItemWebcam.Factory.getSystemWebcam(categoryId, "Milano - Tang Est Mecenate", "http://get.edidomus.it/vp/cam6/image.jpg", 5));
//		mItemsDao.insertWebcam(ItemWebcam.Factory.getSystemWebcam(categoryId, "Milano - Tang Est Carugate", "http://get.edidomus.it/vp/cam8/image.jpg", 5));
//		mItemsDao.insertWebcam(ItemWebcam.Factory.getSystemWebcam(categoryId, "Milano - Tang Est Cologno Monzese", "http://get.edidomus.it/vp/cam9/image.jpg", 5));
//		mItemsDao.insertWebcam(ItemWebcam.Factory.getSystemWebcam(categoryId, "Milano - Tang Est Palmanova", "http://get.edidomus.it/vp/cam10/image.jpg", 5));
//		mItemsDao.insertWebcam(ItemWebcam.Factory.getSystemWebcam(categoryId, "Milano - Tang Ovest San Giuliano", "http://get.edidomus.it/vp/cam3/image.jpg", 5));
//		mItemsDao.insertWebcam(ItemWebcam.Factory.getSystemWebcam(categoryId, "Milano - Tang Ovest raccordo A7", "http://get.edidomus.it/vp/cam5/image.jpg", 5));
//		mItemsDao.insertWebcam(ItemWebcam.Factory.getSystemWebcam(categoryId, "Milano - Tang Ovest Vecchia Vigevanese", "http://get.edidomus.it/vp/cam2/image.jpg", 5));
//		mItemsDao.insertWebcam(ItemWebcam.Factory.getSystemWebcam(categoryId, "Milano - Tang Ovest Nuova Vigevanese", "http://get.edidomus.it/vp/cam7/image.jpg", 5));
//		mItemsDao.insertWebcam(ItemWebcam.Factory.getSystemWebcam(categoryId, "Milano - Tang Ovest uscita SS11 Novara", "http://get.edidomus.it/vp/cam12/image.jpg", 5));
//		mItemsDao.insertWebcam(ItemWebcam.Factory.getSystemWebcam(categoryId, "A4 Torino-Trieste (Cinisello)", "http://get.edidomus.it/vp/cam20/image.jpg", 5));
//		mItemsDao.insertWebcam(ItemWebcam.Factory.getSystemWebcam(categoryId, "A8 Milano-Varese bivio con A9", "http://get.edidomus.it/vp/cam18/image.jpg", 5));
//		mItemsDao.insertWebcam(ItemWebcam.Factory.getSystemWebcam(categoryId, "A8 Milano-Varese direzione Varese", "http://get.edidomus.it/vp/cam19/image.jpg", 5));
//		mItemsDao.insertWebcam(ItemWebcam.Factory.getSystemWebcam(categoryId, "A8 Cerro Maggiore direzione Milano", "http://get.edidomus.it/vp/cam21/image.jpg", 5));
//		mItemsDao.insertWebcam(ItemWebcam.Factory.getSystemWebcam(categoryId, "Torino - uscita Corso Francia", "http://get.edidomus.it/vp/cam24/image.jpg", 5));
//		mItemsDao.insertWebcam(ItemWebcam.Factory.getSystemWebcam(categoryId, "Torino - A32 zona Rivoli direz Frejus", "http://get.edidomus.it/vp/cam25/image.jpg", 5));
//		mItemsDao.insertWebcam(ItemWebcam.Factory.getSystemWebcam(categoryId, "Torino - A32 zona Rivoli direz Tang. O", "http://get.edidomus.it/vp/cam26/image.jpg", 5));
//		mItemsDao.insertWebcam(ItemWebcam.Factory.getSystemWebcam(categoryId, "Autofiori - Savona", "http://get.edidomus.it/vp/cam33/image.jpg", 5));
//		mItemsDao.insertWebcam(ItemWebcam.Factory.getSystemWebcam(categoryId, "Autofiori - Finale Ligure", "http://get.edidomus.it/vp/cam32/image.jpg", 5));
//		mItemsDao.insertWebcam(ItemWebcam.Factory.getSystemWebcam(categoryId, "Autofiori - Albenga", "http://get.edidomus.it/vp/cam31/image.jpg", 5));
//		mItemsDao.insertWebcam(ItemWebcam.Factory.getSystemWebcam(categoryId, "Autofiori - Ventimiglia", "http://get.edidomus.it/vp/cam30/image.jpg", 5));
//		mItemsDao.insertWebcam(ItemWebcam.Factory.getSystemWebcam(categoryId, "Firenze - via De Nicola", "http://get.edidomus.it/vp/cam14/image.jpg", 5));
//		mItemsDao.insertWebcam(ItemWebcam.Factory.getSystemWebcam(categoryId, "Firenze - via Senese", "http://get.edidomus.it/vp/cam16/image.jpg", 5));
//		mItemsDao.insertWebcam(ItemWebcam.Factory.getSystemWebcam(categoryId, "Firenze - via Baccio", "http://get.edidomus.it/vp/cam15/image.jpg", 5));
//		mItemsDao.insertWebcam(ItemWebcam.Factory.getSystemWebcam(categoryId, "Firenze - via Rosselli", "http://get.edidomus.it/vp/cam13/image.jpg", 5));
//		mItemsDao.insertWebcam(ItemWebcam.Factory.getSystemWebcam(categoryId, "GRA uscita Aurelia", "http://get.edidomus.it/vp/cam22/image.jpg", 5));
//		mItemsDao.insertWebcam(ItemWebcam.Factory.getSystemWebcam(categoryId, "GRA via Flaminia", "http://get.edidomus.it/vp/cam28/image.jpg", 5));
//		mItemsDao.insertWebcam(ItemWebcam.Factory.getSystemWebcam(categoryId, "GRA via Appia e Tuscolana", "http://get.edidomus.it/vp/cam27/image.jpg", 5));
//		mItemsDao.insertWebcam(ItemWebcam.Factory.getSystemWebcam(categoryId, "Roma Fiumicino entrata EUR", "http://get.edidomus.it/vp/cam29/image.jpg", 5));
//
//		categoryId = mItemsDao.insertCategory(ItemCategory.Factory.getSystemCategory(0, "Italy places"));
//		mItemsDao.insertWebcam(ItemWebcam.Factory.getSystemWebcam(categoryId, "(AN) Riviera del Conero", "http://www.damablu.it/video.jpg", 10));
//		mItemsDao.insertWebcam(ItemWebcam.Factory.getSystemWebcam(categoryId, "(AO) Aosta - Arco d'Augusto", "http://www.regione.vda.it/Bollettino_neve/Images/ao2.jpg", 60));
//		mItemsDao.insertWebcam(ItemWebcam.Factory.getSystemWebcam(categoryId, "(AO) Valtournenche - piste da sci", "http://www.regione.vda.it/Bollettino_neve/Images/valtour.jpg", 60));
//		mItemsDao.insertWebcam(ItemWebcam.Factory.getSystemWebcam(categoryId, "(BO) Bologna panoramica 1", "http://www.bolognameteo.it/broadcast.jpg", 60));
//		mItemsDao.insertWebcam(ItemWebcam.Factory.getSystemWebcam(categoryId, "(BO) Bologna - piazza Le Due Torri", "http://www.baskerville.it/webcam/live.jpg", 60));
//		mItemsDao.insertWebcam(ItemWebcam.Factory.getSystemWebcam(categoryId, "(BS) Brescia - Castello panorama", "http://www.starrylink.it/webcam/brescia/brescia.jpg", 60));
//		mItemsDao.insertWebcam(ItemWebcam.Factory.getSystemWebcam(categoryId, "(BZ) Valle Aurina", "http://speikboden.it-wms.com/panorama1.jpg", 60));
//		mItemsDao.insertWebcam(ItemWebcam.Factory.getSystemWebcam(categoryId, "(CT) Etna", "http://www.albanetcom.com/etnaimg/cam_00001.jpg", 60));
//		mItemsDao.insertWebcam(ItemWebcam.Factory.getSystemWebcam(categoryId, "(CT) Etna Sud", "http://www.etnatech.com/camupload/etnasud.jpg", 60));
//		mItemsDao.insertWebcam(ItemWebcam.Factory.getSystemWebcam(categoryId, "(GE) Genova panoramica", "http://www.stefanome.it/current.jpg", 60));
//		mItemsDao.insertWebcam(ItemWebcam.Factory.getSystemWebcam(categoryId, "(GE) Genova - Piazza Verdi", "http://www.tu6genova.it/immagini/13.jpg", 60));
//		mItemsDao.insertWebcam(ItemWebcam.Factory.getSystemWebcam(categoryId, "(GE) Genova - via Diaz", "http://www.tu6genova.it/immagini/4.jpg", 60));
//		mItemsDao.insertWebcam(ItemWebcam.Factory.getSystemWebcam(categoryId, "(GE) Genova - corso Europa", "http://www.tu6genova.it/immagini/16.jpg", 60));
//		mItemsDao.insertWebcam(ItemWebcam.Factory.getSystemWebcam(categoryId, "(FI) Firenza - Duomo", "http://www.italywebcams.it/kraft/florence.jpg", 60));
//		mItemsDao.insertWebcam(ItemWebcam.Factory.getSystemWebcam(categoryId, "(FO) Foligno panoramica", "http://www.umbriameteo.com/web2/images/last640.jpg", 60));
//		mItemsDao.insertWebcam(ItemWebcam.Factory.getSystemWebcam(categoryId, "(LT) Ponza panoramica", "http://www.agropontino.it/webcam/test71.jpg", 60));
//		mItemsDao.insertWebcam(ItemWebcam.Factory.getSystemWebcam(categoryId, "(IM) Ventimiglia - Torre comunale", "http://www.comune.ventimiglia.it/webcamcomune/webcam.jpg", 60));
//		mItemsDao.insertWebcam(ItemWebcam.Factory.getSystemWebcam(categoryId, "(IM) Ventimiglia - Forte dell'Annunziata", "http://www.comune.ventimiglia.it/webcamcomune/museo.jpg", 60));
//		mItemsDao.insertWebcam(ItemWebcam.Factory.getSystemWebcam(categoryId, "(MC) Monte Prata", "http://www.meteoappennino.it/webprata/images/last1024.jpg", 60));
//		mItemsDao.insertWebcam(ItemWebcam.Factory.getSystemWebcam(categoryId, "(MC) Frontignano", "http://www.meteoappennino.it/webfrontignano/images/zoom_b1024.jpg", 60));
//		mItemsDao.insertWebcam(ItemWebcam.Factory.getSystemWebcam(categoryId, "(MI) Milano - piazza San Babila", "http://www.imb.it/fullsize2.jpg", 60));
//		mItemsDao.insertWebcam(ItemWebcam.Factory.getSystemWebcam(categoryId, "(MI) Milano - Corso Buenos Aires", "http://www.fashionschool.com/fashion_school.jpg", 60));
//		mItemsDao.insertWebcam(ItemWebcam.Factory.getSystemWebcam(categoryId, "(NA) Napoli - Golfo e Vesuvio", "http://www.salernometeo.it/Webcam/napoli/currentsmall.jpg", 30));
//		mItemsDao.insertWebcam(ItemWebcam.Factory.getSystemWebcam(categoryId, "(NA) Ischia Ponte", "http://www.ischiaonline.it/cams/heuropa/heuropa.jpg", 60));
//		mItemsDao.insertWebcam(ItemWebcam.Factory.getSystemWebcam(categoryId, "(PG) Castelluccio di Norcia - Monte Vettore", "http://www.umbriameteo.com/web1/images/last800.jpg", 60));
//		mItemsDao.insertWebcam(ItemWebcam.Factory.getSystemWebcam(categoryId, "(PG) Perugia, piazza IV Novembre", "http://www.comune.perugia.it/livecams/hugesize.jpg", 60));
//		mItemsDao.insertWebcam(ItemWebcam.Factory.getSystemWebcam(categoryId, "(PU) Monte Nerone", "http://www.meteoappennino.it/webmontenerone/images/montenerone_1024.jpg", 60));
//		mItemsDao.insertWebcam(ItemWebcam.Factory.getSystemWebcam(categoryId, "(RM) Roma - panoramica San Piertro", "http://www.barcello.it/images/meteo/axis.jpg", 60));
//		mItemsDao.insertWebcam(ItemWebcam.Factory.getSystemWebcam(categoryId, "(RN) Riccione - Spiaggia 61 della Rosa", "http://www.dellarosae.191.it/spiaggia61.jpg", 60));
//		mItemsDao.insertWebcam(ItemWebcam.Factory.getSystemWebcam(categoryId, "(RO) Adria - Circuito internazionale", "http://80.206.235.242/record/current.jpg", 60));
//		mItemsDao.insertWebcam(ItemWebcam.Factory.getSystemWebcam(categoryId, "(SA) Positano - la spiaggia", "http://www.campaniameteo.it/webcampositano/pos002.jpg", 60));
//		mItemsDao.insertWebcam(ItemWebcam.Factory.getSystemWebcam(categoryId, "(SA) Positano - zona alta", "http://www.campaniameteo.it/webcampositano/zoo001.jpg", 60));
//		mItemsDao.insertWebcam(ItemWebcam.Factory.getSystemWebcam(categoryId, "(SO) Passo dello Stelvio - funivia", "http://jpeg.popso.it/webcam/webcam_online/stelviolive_01.jpg", 60));
//		mItemsDao.insertWebcam(ItemWebcam.Factory.getSystemWebcam(categoryId, "(TE) Prati di Tivo", "http://www.meteoappennino.it/webpratiditivo/images/pratiditivo_zoom_b1024.jpg", 60));
//		mItemsDao.insertWebcam(ItemWebcam.Factory.getSystemWebcam(categoryId, "(TE) Prato Selva", "http://www.meteoappennino.it/webpratoselva/images/pratoselva_1024.jpg", 60));
//		mItemsDao.insertWebcam(ItemWebcam.Factory.getSystemWebcam(categoryId, "(TO) Torino - Duomo Cappella della Sacra Sindone e Mole Antonelliana", "http://www.comune.torino.it/telecamera/duomo/duomof.jpg", 60));
//		mItemsDao.insertWebcam(ItemWebcam.Factory.getSystemWebcam(categoryId, "(TO) Torino - Piazza San Giovanni Porta Palatina ", "http://get.edidomus.it/vp/cam2/image.jpg", 60));
//		mItemsDao.insertWebcam(ItemWebcam.Factory.getSystemWebcam(categoryId, "(TO) Torino - Palazzo Civico Piazza Palazzo di Citta", "http://www.comune.torino.it/telecamera/pcivico/civicof.jpg", 60));
//		mItemsDao.insertWebcam(ItemWebcam.Factory.getSystemWebcam(categoryId, "(TO) Torino - Panoramica 1", "http://www.comune.torino.it/telecamera/meucci/meucci1f.jpg", 60));
//		mItemsDao.insertWebcam(ItemWebcam.Factory.getSystemWebcam(categoryId, "(TO) Torino - Panoramica 2", "http://www.comune.torino.it/telecamera/meucci/meucci2f.jpg", 60));
//		mItemsDao.insertWebcam(ItemWebcam.Factory.getSystemWebcam(categoryId, "(TO) Torino - Mole Antonelliana", "http://danielemeteo.altervista.org/webcam.php", 60));
//		mItemsDao.insertWebcam(ItemWebcam.Factory.getSystemWebcam(categoryId, "(TR) Trieste - porto", "http://151.8.71.28/~www/COMMON/WEBCAM/WebcamTrieste.jpg", 60));
//		mItemsDao.insertWebcam(ItemWebcam.Factory.getSystemWebcam(categoryId, "(TR) Trieste piazza Verdi", "http://www.commissariato.fvg.it/webcam/piazzaverdi.jpg", 60));
//		mItemsDao.insertWebcam(ItemWebcam.Factory.getSystemWebcam(categoryId, "(PI) Pisa - Torre dell'Orologio", "http://www.comune.pisa.it/webcam/img/pisa.jpg", 5));
//		mItemsDao.insertWebcam(ItemWebcam.Factory.getSystemWebcam(categoryId, "(SI) San Gimignano - Piazza dell'Erbe", "http://www.divineria.it/IPwwebcam/webcam.jpg", 60));
//		mItemsDao.insertWebcam(ItemWebcam.Factory.getSystemWebcam(categoryId, "(VE) Venezia - Canal Grande", "http://turismo.regione.veneto.it/webcam/huge.jpg", 60));
//		mItemsDao.insertWebcam(ItemWebcam.Factory.getSystemWebcam(categoryId, "(VE) Venezia - piazza San Marco", "http://www2.comune.venezia.it/webCamPsm/imgpsm_00001.jpg", 60));
//		mItemsDao.insertWebcam(ItemWebcam.Factory.getSystemWebcam(categoryId, "(VE) Venezia - Ponte di Rialto", "http://www.camsturion.com/sturion.jpg", 60));
//		//mItemsDao.insertWebcam(ItemWebcam.Factory.getSystemWebcam(categoryId, "", "", 60));
//
//		categoryId = mItemsDao.insertCategory(ItemCategory.Factory.getSystemCategory(0, "Beautiful places"));
//		mItemsDao.insertWebcam(ItemWebcam.Factory.getSystemWebcam(categoryId, "Canarie - Jandia Beach", "http://www.restaurantecoronado.com/webcams/coronadobeach2.jpg", 60));
//		mItemsDao.insertWebcam(ItemWebcam.Factory.getSystemWebcam(categoryId, "Egypt - Giza plain", "http://www.pyramidcam.com/netcam.jpg", 60));
//		mItemsDao.insertWebcam(ItemWebcam.Factory.getSystemWebcam(categoryId, "Canada - Niagara Waterfalls", "http://www.fallsview.com/Stream/camera0.jpg", 60));
//		mItemsDao.insertWebcam(ItemWebcam.Factory.getSystemWebcam(categoryId, "Australia - Parkers Radio Telescope", "http://outreach.atnf.csiro.au/visiting/parkes/webcam/parkes.full.jpg", 30));
//		mItemsDao.insertWebcam(ItemWebcam.Factory.getSystemWebcam(categoryId, "South Dakota - Mount Rushmore", "http://media.sd.gov:88/webcam/rushmore_00001.jpg", 60));
//		mItemsDao.insertWebcam(ItemWebcam.Factory.getSystemWebcam(categoryId, "St. Barths - St-Jean's Bay", "http://www.st-barths.com/a_cam/view1.jpg", 60));
//		mItemsDao.insertWebcam(ItemWebcam.Factory.getSystemWebcam(categoryId, "Florida - Palm Beach Jupiter Inlet", "http://evsjupiter.netfirms.com/zoo001t.jpg", 60));
//		mItemsDao.insertWebcam(ItemWebcam.Factory.getSystemWebcam(categoryId, "Yosemite - Glacier Point", "http://www.yosemite.org/vryos/turtleback1.jpg", 60));
//		mItemsDao.insertWebcam(ItemWebcam.Factory.getSystemWebcam(categoryId, "Colorado River - Red Cliffs Lodge", "http://www.cnha.org/webcam/image.jpg", 60));
//		mItemsDao.insertWebcam(ItemWebcam.Factory.getSystemWebcam(categoryId, "Grand Canyon - Yavapai Point", "http://www2.nature.nps.gov/air/webcams/parks/grcacam/grca.jpg", 60));
//		mItemsDao.insertWebcam(ItemWebcam.Factory.getSystemWebcam(categoryId, "Yellow Stone - Old Faithfull Geyser", "http://64.241.25.110/yell/webcams/oldfaith2.jpg", 60));
//		mItemsDao.insertWebcam(ItemWebcam.Factory.getSystemWebcam(categoryId, "Pikes Peak Cam", "http://www.pikespeakcam.com/images/cam.jpg", 60));
//		//mItemsDao.insertWebcam(ItemWebcam.Factory.getSystemWebcam(categoryId, "", "", 60));
//
//
//		categoryId = mItemsDao.insertCategory(ItemCategory.Factory.getSystemCategory(0, "World Cities"));
//		mItemsDao.insertWebcam(ItemWebcam.Factory.getSystemWebcam(categoryId, "Athens - panorama", "http://nifada.com/webcam/current.jpg", 60));
//		mItemsDao.insertWebcam(ItemWebcam.Factory.getSystemWebcam(categoryId, "Honk Kong - the Peak", "http://www.discoverhongkong.com/eng/interactive/webcam/images/ig_webc_peak1.jpg", 60));
//		mItemsDao.insertWebcam(ItemWebcam.Factory.getSystemWebcam(categoryId, "Honk Kong - Victoria Park", "http://www.discoverhongkong.com/eng/interactive/webcam/images/ig_webc_vict1.jpg", 60));
//		mItemsDao.insertWebcam(ItemWebcam.Factory.getSystemWebcam(categoryId, "Honk Kong - Causeway Bay", "http://www.discoverhongkong.com/eng/interactive/webcam/images/ig_webc_caus1.jpg", 60));
//		mItemsDao.insertWebcam(ItemWebcam.Factory.getSystemWebcam(categoryId, "Honk Kong - Victoria Harbour", "http://www.discoverhongkong.com/eng/interactive/webcam/images/ig_webc_harb1.jpg", 60));
//		mItemsDao.insertWebcam(ItemWebcam.Factory.getSystemWebcam(categoryId, "Hong Kong - skyline from Admiralty", "http://www.discoverhongkong.com/eng/interactive/webcam/images/ig_webc_petr1.jpg", 60));
//		mItemsDao.insertWebcam(ItemWebcam.Factory.getSystemWebcam(categoryId, "Shangai - Grand Gateway", "http://www.vuille.com/photo.jpg", 60));
//		mItemsDao.insertWebcam(ItemWebcam.Factory.getSystemWebcam(categoryId, "Shanghai - panorama", "http://www.ds-shanghai.org.cn/webcam/image.jpg", 60));
//		mItemsDao.insertWebcam(ItemWebcam.Factory.getSystemWebcam(categoryId, "Paris - Tour Eiffel", "http://www.parislive.net/eiffelwebcam1.jpg", 60));
//		mItemsDao.insertWebcam(ItemWebcam.Factory.getSystemWebcam(categoryId, "Paris - Tour Eiffel big", "http://www.parislive.net/eiffelcam3.jpg", 60));
//		mItemsDao.insertWebcam(ItemWebcam.Factory.getSystemWebcam(categoryId, "Paris - Pantheon", "http://webcam.ville.woob2.com/Pantheon_full.jpg", 60));
//		mItemsDao.insertWebcam(ItemWebcam.Factory.getSystemWebcam(categoryId, "Parigi - panorama", "http://www-compat.tf1.fr/webcam/file222.jpg", 60));
//		mItemsDao.insertWebcam(ItemWebcam.Factory.getSystemWebcam(categoryId, "New York - Fifth Avenue", "http://www.mte.com/webcam/5thave.jpg", 30));
//		mItemsDao.insertWebcam(ItemWebcam.Factory.getSystemWebcam(categoryId, "New York - Manhattan", "http://www.nyvibe.net/nyvibescam/view.jpg", 60));
//		mItemsDao.insertWebcam(ItemWebcam.Factory.getSystemWebcam(categoryId, "London - Covent Garden", "http://londonwebcam.virtual-london.com/cam.jpg", 30));
//		mItemsDao.insertWebcam(ItemWebcam.Factory.getSystemWebcam(categoryId, "Barcelona - panorama", "http://www.tvcatalunya.com/webcams/noves/arts2.jpg", 60));
//		mItemsDao.insertWebcam(ItemWebcam.Factory.getSystemWebcam(categoryId, "Dallas - skyline", "http://www.wfaa.com/sharedcontent/dws/img/standing/cams/wfaahuge.jpg", 60));
//		mItemsDao.insertWebcam(ItemWebcam.Factory.getSystemWebcam(categoryId, "Bangkok - skyline", "http://webcam.ose-software.com/video.jpg", 60));
//		mItemsDao.insertWebcam(ItemWebcam.Factory.getSystemWebcam(categoryId, "Detroit - skyline", "http://images.ibsys.com/det/images/weather/auto/windsorcam_640x480.jpg", 60));
//		mItemsDao.insertWebcam(ItemWebcam.Factory.getSystemWebcam(categoryId, "Moscow - Kremlin", "http://webcam.mdmbank.ru/webcam/images/fullsize.jpg", 60));
//		mItemsDao.insertWebcam(ItemWebcam.Factory.getSystemWebcam(categoryId, "Ottawa - Parliament Hill", "http://www.parliamenthill.gc.ca/text/newhillcam.jpg", 60));
//		mItemsDao.insertWebcam(ItemWebcam.Factory.getSystemWebcam(categoryId, "Vancouver - The lake", "http://www.katkam.ca/pix/pic.jpg", 60));
//		mItemsDao.insertWebcam(ItemWebcam.Factory.getSystemWebcam(categoryId, "Sapporo - panorama", "http://www.hbc.co.jp/videont2/sappo_l.jpg", 60));
//		mItemsDao.insertWebcam(ItemWebcam.Factory.getSystemWebcam(categoryId, "Monaco di Baviera - Marienplatz webcam", "http://www.muenchner-freiheit.net/fiveminutes/marienplatz-1.jpg", 60));
//		mItemsDao.insertWebcam(ItemWebcam.Factory.getSystemWebcam(categoryId, "Dublino - O'Connell Bridge", "http://www.ireland.com/includes/webcam/liveview.jpg", 60));
//		mItemsDao.insertWebcam(ItemWebcam.Factory.getSystemWebcam(categoryId, "Tallin (Estony) - panorama", "http://www.ilm.ee/tallinn/image.jpg", 60));
//		mItemsDao.insertWebcam(ItemWebcam.Factory.getSystemWebcam(categoryId, "Vienna - Rathausplatz", "http://www.wien.gv.at/camera/rathausplatz.jpg", 60));
//		mItemsDao.insertWebcam(ItemWebcam.Factory.getSystemWebcam(categoryId, "Helsinki - South Harbour", "http://www.ek.fi/kamera/palace00.jpg", 60));
//		mItemsDao.insertWebcam(ItemWebcam.Factory.getSystemWebcam(categoryId, "Riyadh (K.S.A.) - King Fahd Road", "http://my.saudi.net.sa/IMAGES/road.jpg", 60));
//		mItemsDao.insertWebcam(ItemWebcam.Factory.getSystemWebcam(categoryId, "Manhattan skyline", "http://abclocal.go.com/three/wabc/webcam/skycpk.jpg", 60));
//		mItemsDao.insertWebcam(ItemWebcam.Factory.getSystemWebcam(categoryId, "Toronto skyline", "http://www.2ontario.com/webcam/oissouth.jpg", 60));
//		mItemsDao.insertWebcam(ItemWebcam.Factory.getSystemWebcam(categoryId, "Ibiza - panorama", "http://www.toibiza.com/html/images/webcam/capture1.jpg", 60));
//		mItemsDao.insertWebcam(ItemWebcam.Factory.getSystemWebcam(categoryId, "San Francisco - Golden Gate", "http://static.cbslocal.com/cbs/kpix/webcams/ggb_nu.jpg", 60));
//		mItemsDao.insertWebcam(ItemWebcam.Factory.getSystemWebcam(categoryId, "Buenos Aire - sky view", "http://itaucam.itau.com.ar/fullsize.jpg", 60));
//		mItemsDao.insertWebcam(ItemWebcam.Factory.getSystemWebcam(categoryId, "Indiana - Notre Dame Dome And Basilica", "http://www.nd.edu/~webcam/domecam.jpg", 60));
//		mItemsDao.insertWebcam(ItemWebcam.Factory.getSystemWebcam(categoryId, "Washington, D.C.", "http://media.washingtonpost.com/media/webcams/webcam32.jpg", 60));
	}
}
