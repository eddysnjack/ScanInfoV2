package com.androeddy.scaninfov2;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.androeddy.scaninfov2.adapters.CustomExpandableListAdapter;
import com.androeddy.scaninfov2.connections.AsyncGetWithJsoup;
import com.androeddy.scaninfov2.connections.AsyncGetWithJsoupMulti;
import com.androeddy.scaninfov2.connections.DatabaseLayer;
import com.androeddy.scaninfov2.statics.CaptureActivityPortrait;
import com.androeddy.scaninfov2.statics.DBTableSearchHistory;
import com.androeddy.scaninfov2.statics.LinkLayer;
import com.androeddy.scaninfov2.statics.MedicineInfoObject;
import com.androeddy.scaninfov2.statics.MedicineUrlObject;
import com.androeddy.scaninfov2.statics.URLTypes;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class MainActivity extends AppCompatActivity {
    ImageButton imgBtnScanner;
    Button mainActivity_button_search/*, mainActivity_button_test*/;
    SearchView InputSearch;
    ProgressBar progressBar;
    TextView textview_warning_messages,
            mainActivity_text_medicineActiveIngredient,
            mainActivity_text_medicineBarcode,
            mainActivity_text_medicineFirm,
            mainActivity_text_medicineName,
            mainActivity_text_medicinePieces,
            mainActivity_text_medicinePrescription;
    TableLayout mainActivity_part_homeScreen;
    LinearLayout mainActivity_part_resultScreen;
    MedicineInfoObject globalMedicineInfo;
    ExpandableListView mainActivity_expandableListMedicine;
    ImageButton mainActivity_button_Home;
    Toolbar mainActivity_toolbar;

    public static final String keyKTnedirNeicinKullanilir = "Nedir Ne İçin Kullanılır";
    public static final String keyKTnasilKullanilir = "Nasıl Kullanılır";
    public static final String keyKTkullanmadanOnceDikkat = "Kullanmadan Önce Dikkat Edilmesi Gerekenler";
    public static final String keyKTyanEtkiler = "Yan Etkiler";
    public static final String keyilacProspektusuDozaj = "İlaç Prospektüsü Dozaj";
    public static final String keyilacProspektusuEndikasyonlari = "İlaç Prospektüsü Endikasyonları";
    public static final String keyilacProspektusuEtkilesimler = "İlaç Prospektüsü Etkileşimler";
    public static final String keyilacProspektusuKontrendikasyonlari = "İlaç Prospektüsü Kontrendikasyonları";
    public static final String keyilacProspektusuYanEtkiler = "İlaç Prospektüsü Yan Etkiler";

    //------------------------------------------ MAIN ------------------------------------------------//
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainActivity_button_search = findViewById(R.id.mainActivity_button_search);
        imgBtnScanner = findViewById(R.id.imgBtnScanner);
        InputSearch = findViewById(R.id.InputSearchID);
        progressBar = findViewById(R.id.ProBar);
        textview_warning_messages = findViewById(R.id.mainActivity_textview_warnings_and_messages);
        mainActivity_part_homeScreen = findViewById(R.id.mainActivity_part_homeScreen);
        mainActivity_part_resultScreen = findViewById(R.id.mainActivity_part_resultScreen);
        //mainActivity_button_test = findViewById(R.id.mainActivity_button_test);
        mainActivity_text_medicineActiveIngredient = findViewById(R.id.mainActivity_text_medicineActiveIngredient);
        mainActivity_text_medicineBarcode = findViewById(R.id.mainActivity_text_medicineBarcode);
        mainActivity_text_medicineFirm = findViewById(R.id.mainActivity_text_medicineFirm);
        mainActivity_text_medicineName = findViewById(R.id.mainActivity_text_medicineName);
        mainActivity_text_medicinePieces = findViewById(R.id.mainActivity_text_medicinePieces);
        mainActivity_text_medicinePrescription = findViewById(R.id.mainActivity_text_medicinePrescription);
        mainActivity_expandableListMedicine = findViewById(R.id.mainActivity_expandableListMedicine);
        mainActivity_button_Home = findViewById(R.id.mainActivity_button_Home);
        mainActivity_toolbar = findViewById(R.id.mainActivity_toolbar);
        setSupportActionBar(mainActivity_toolbar);
        //other settings
        globalMedicineInfo = new MedicineInfoObject();
        textview_warning_messages.setText("");
        fillTheHomeList();
        ///------------ test amaçlı
//        mainActivity_button_test.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
//------------------------------------ Button Click Listeners ------------------------------------//
        //Scanner button function
        imgBtnScanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScannerButtonClicked(v);
            }
        });

        //Name search button function
        mainActivity_button_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!TextUtils.isEmpty(InputSearch.getQuery().toString())) { // eğer boş değilse/ if its not empty
                    String url = "https://www.google.com/search?q=site:https://www.ilacrehberi.com/%20";
                    url += InputSearch.getQuery(); //arrange the url
                    MakeTheSearch gp = new MakeTheSearch(); //asynctask object
                    gp.execute(url); //start the asynctask
                } else { // if its empty show the hint
                    InputSearch.setQueryHint(getString(R.string.searchview_empty_notification_hint));
                }
            }
        });

        //search box submit method / klavye ekranında arama tuşunu aktifleştirme
        InputSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                CloseTheKeyboard();
                mainActivity_button_search.performClick();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        mainActivity_button_Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainActivity_part_resultScreen.setVisibility(View.GONE);
                mainActivity_part_homeScreen.setVisibility(View.VISIBLE);
                textview_warning_messages.setText("");
            }
        });
    }
//============================================================= NATIVE METHOD ENDS

    private void setUrlsOfHomeScreen(HashMap<String, String> NameAndUrls) {
        if (NameAndUrls == null) {
            System.out.println("setUrlsOfHomeScreen: NameAndUrls is EMPTY");
            Toast.makeText(this, "setUrlsOfHomeScreen: NameAndUrls is EMPTY", Toast.LENGTH_SHORT).show();
            return;
        }
        int problemCounter = 0;

        Iterator iterator = NameAndUrls.entrySet().iterator();
        for (int i = 0; i < mainActivity_part_homeScreen.getChildCount(); i++) {
            View tableRow = mainActivity_part_homeScreen.getChildAt(i);
            if (tableRow instanceof TableRow) {
                int count = ((TableRow) tableRow).getChildCount();
                for (int j = 0; j < count; j++) {
                    if (iterator.hasNext()) {
                        CardView cardView = (CardView) ((TableRow) tableRow).getChildAt(j);
                        TextView textView_Name = (TextView) cardView.getChildAt(0);
                        TextView textView_Url = (TextView) cardView.getChildAt(1);
                        Map.Entry entry = (Map.Entry) iterator.next();
                        textView_Name.setText(entry.getKey().toString());
                        textView_Url.setText(entry.getValue().toString());
                        System.out.println("Setting click Listener");
                        cardView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                handleTableClicks(view);
                            }
                        });
                    } else {
                        problemCounter++;
                        System.out.println("problemCounter Çalıştı");
                    }
                }
            } else {
                System.out.println("it is NOT a tablerow");
            }
        }

        if (problemCounter == 0) {
            mainActivity_part_homeScreen.setVisibility(View.VISIBLE);
        } else {
            mainActivity_part_homeScreen.setVisibility(View.GONE);
        }
    }

    private void handleTableClicks(View view) {
        if (view instanceof CardView) {
            CardView cardView = (CardView) view;
            TextView secondChild = (TextView) cardView.getChildAt(1);
            String urlAddressOfMedicine = secondChild.getText().toString();
            System.out.println("URL ADRESİ " + urlAddressOfMedicine);

            getMedicineResultsAndShowDetails("https://" + urlAddressOfMedicine);//url'lerin başında protokol yok
        } else {
            System.out.println("handleTableClicks NOT A CARDVIEW");
        }
    }

    // -----MEDICINE_DATA_CHAIN_1
    private void getMedicineResultsAndShowDetails(String urlAddressOfMedicine) {
        //if we are here then clear the fields first
        clearTheResultFields();
        AsyncGetWithJsoup.AsyncResponse asyncResponse = new AsyncGetWithJsoup.AsyncResponse() {
            @Override
            public void processFinish(Document output, int position) {
                // -- hide other widgets
                mainActivity_part_homeScreen.setVisibility(View.GONE);
                // -- show result widgets
                mainActivity_part_resultScreen.setVisibility(View.VISIBLE);
                //Get all the information about the medicine
                getAllMedicineData(output);
                // -- fill necessary fields
            }
        };

        AsyncGetWithJsoup asyncGetWithJsoup = new AsyncGetWithJsoup(asyncResponse);
        asyncGetWithJsoup.execute(urlAddressOfMedicine);
    }

    private void clearTheResultFields() {
        mainActivity_text_medicineActiveIngredient.setText("");
        mainActivity_text_medicineBarcode.setText("");
        mainActivity_text_medicineFirm.setText("");
        mainActivity_text_medicineName.setText("");
        mainActivity_text_medicinePieces.setText("");
        mainActivity_text_medicinePrescription.setText("");
        //listeyi boşalt. direk null olmuyor. https://stackoverflow.com/questions/21114346/clear-expandablelistview/30301383
        mainActivity_expandableListMedicine.setAdapter((BaseExpandableListAdapter) null);
    }

    // -----MEDICINE_DATA_CHAIN_2
    private void getAllMedicineData(Document document) {
        //ilaç bilgisi tablosu div.col-right > table(2.tablo) > içerisinde <tr> <td>.. <td>.. </tr> şeklinde. iki sütünlu kolonlarda müteşekkil
        System.out.println("=========================== İLAÇ BILGISIIIIIIIIIIIIIIIIIIIIIIIIIIIIII");
        //get name
        String headerElementText = document.select("div.col-right > h1").text();
        System.out.println("headerElementText =" + headerElementText);
        String medicineName = headerElementText.replace("ilaç prospektüsü", "");
        System.out.println("ilaç adı " + medicineName);
        globalMedicineInfo.setName(medicineName);


        Elements medicineInfoTableBody = document.select("div.col-right > div > table#one-column-emphasisc > tbody > tr");
        for (Element row : medicineInfoTableBody) {
            Element firstCell = row.selectFirst("td:eq(0)");
            Element secondCell = row.selectFirst("td:eq(1)");

            String firstCellContent = firstCell.toString();
            if (firstCellContent.toLowerCase().contains("etkin madde")) {
                globalMedicineInfo.setActiveIngredient(secondCell.text());
                System.out.println("etkin madde" + secondCell.text());
            } else if (firstCellContent.toLowerCase().contains("logo") && firstCellContent.toLowerCase().contains("img") || firstCellContent.contains("align=\"left\" valign=\"top\"")) {
                String firmName = secondCell.selectFirst("h2").text();
                globalMedicineInfo.setFirm(firmName);
                System.out.println("firma" + firmName);

            } else if (firstCellContent.toLowerCase().contains("barkodu")) {
                String barcodeText = secondCell.attr("title");//barcode num is end of the string
                String words[] = barcodeText.split(" ");
                String barcodeNum = words[words.length - 1];
                globalMedicineInfo.setBarcode(barcodeNum);
                System.out.println("barcod " + barcodeNum);
            } else if (firstCellContent.toLowerCase().contains("ambalaj miktarı")) {
                globalMedicineInfo.setPieces(secondCell.text());
                System.out.println("miktarı " + secondCell.text());
            } else if (firstCellContent.toLowerCase().contains("reçete durumu")) {
                System.out.println("reçete duurmu " + secondCell.ownText());
                globalMedicineInfo.setPrescription(secondCell.ownText());
            }
        }

        //set overview data
        mainActivity_text_medicineActiveIngredient.setText(globalMedicineInfo.getActiveIngredient());
        mainActivity_text_medicineBarcode.setText(globalMedicineInfo.getBarcode());
        mainActivity_text_medicineFirm.setText(globalMedicineInfo.getFirm());
        mainActivity_text_medicineName.setText(globalMedicineInfo.getName());
        mainActivity_text_medicinePieces.setText(globalMedicineInfo.getPieces());
        mainActivity_text_medicinePrescription.setText(globalMedicineInfo.getPrescription());
        //save to history
        DatabaseLayer db = new DatabaseLayer(this);
        Date currentdate = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
        db.saveSearchHistory(
                new DBTableSearchHistory(
                        globalMedicineInfo.getName(),
                        globalMedicineInfo.getBarcode(),
                        sdf.format(currentdate)
                )
        );

        MedicineUrlObject medicineUrlObject = getMedicineUsageLiks(document);
        if (medicineUrlObject.getUrlType() == URLTypes.notFound) {
            System.out.println("getAllMedicineData: KULLANIM DETAYLRI İÇİN KAYIT BULUNAMADI");
            textview_warning_messages.setText("Kullanım detayları için kayıt bulunamadı");

        } else if (medicineUrlObject.getUrlType() == URLTypes.kt) {
            textview_warning_messages.setText("Kullanım Talimatı Gösteriliyor");
            getMedicineUsageData(medicineUrlObject);
        } else if (medicineUrlObject.getUrlType() == URLTypes.prescription) {
            textview_warning_messages.setText("Kullanım Talimatı BULUNAMADI Prospektüs Gösteriliyor");
            getMedicineUsageData(medicineUrlObject);
        }
    }

    private MedicineUrlObject getMedicineUsageLiks(Document document) {
        //eğer KT-kullanma talimatı- varsa onu kullan
        Elements tableKTandKUBandPR = document.select("div.col-right > table > tbody > tr>td");
        int flag = 0;
        HashMap<String, String> keysAndUrls = new HashMap<>();
        MedicineUrlObject medicineUrlObject = new MedicineUrlObject();
        for (Element element : tableKTandKUBandPR) {
            String href = element.getElementsByTag("a").attr("href");
            if (href.contains("/kt/")) {//kullanma talimatı
                flag = 1;
                break;
            }

            if (href.contains("prospektusu")) {//prospektüs için
                flag = 2;
            }
        }

        //kt bizim için daha iyi. daha önemli
        if ((flag & 1) == 1) {//kt vardır
            //linkleri ayarla
            String baseUrl = document.baseUri();
            String nedirNeicinKullanilirURL = baseUrl + LinkLayer.KT_NEDIR_VE_NE_ICIN_KULLANILIR;
            String nasilKullanilirURL = baseUrl + LinkLayer.KT_NASIL_KULLANILIR;
            String kullanmadanOnceDikkatURL = baseUrl + LinkLayer.KT_KULLANMADAN_ONCE_DIKKAT_EDILMESI_GEREKENLER;
            String yanEtkilerURL = baseUrl + LinkLayer.KT_YAN_ETKILERI;
            keysAndUrls.put(keyKTnedirNeicinKullanilir, nedirNeicinKullanilirURL);
            keysAndUrls.put(keyKTnasilKullanilir, nasilKullanilirURL);
            keysAndUrls.put(keyKTkullanmadanOnceDikkat, kullanmadanOnceDikkatURL);
            keysAndUrls.put(keyKTyanEtkiler, yanEtkilerURL);
            medicineUrlObject.setUrlData(keysAndUrls);
            medicineUrlObject.setUrlType(URLTypes.kt);
        } else if ((flag & 2) == 2) {//propektus vardır
            //linkleri ayarla
            String baseUrl = document.baseUri();
            String ilacProspektusuDozaj = baseUrl + LinkLayer.ILAC_PROSPEKTUSU_DOZAJ;
            String ilacProspektusuEndikasyonlari = baseUrl + LinkLayer.ILAC_PROSPEKTUSU_ENDIKASYONLARI;
            String ilacProspektusuEtkilesimler = baseUrl + LinkLayer.ILAC_PROSPEKTUSU_ETKILESIMLER;
            String ilacProspektusuKontrendikasyonlari = baseUrl + LinkLayer.ILAC_PROSPEKTUSU_KONTRENDIKASYONLARI;
            String ilacProspektusuYanEtkiler = baseUrl + LinkLayer.ILAC_PROSPEKTUSU_YAN_ETKILER;
            keysAndUrls.put(keyilacProspektusuDozaj, ilacProspektusuDozaj);
            keysAndUrls.put(keyilacProspektusuEndikasyonlari, ilacProspektusuEndikasyonlari);
            keysAndUrls.put(keyilacProspektusuEtkilesimler, ilacProspektusuEtkilesimler);
            keysAndUrls.put(keyilacProspektusuKontrendikasyonlari, ilacProspektusuKontrendikasyonlari);
            keysAndUrls.put(keyilacProspektusuYanEtkiler, ilacProspektusuYanEtkiler);
            medicineUrlObject.setUrlData(keysAndUrls);
            medicineUrlObject.setUrlType(URLTypes.prescription);
        } else {
            medicineUrlObject.setUrlData(null);
            medicineUrlObject.setUrlType(URLTypes.notFound);
        }

        return medicineUrlObject;
    }

    // -----MEDICINE_DATA_CHAIN_3
    private void getMedicineUsageData(MedicineUrlObject medicineUrlObject) {
        final int urlType = medicineUrlObject.getUrlType();
        AsyncGetWithJsoupMulti.AsyncResponse asyncResponse = new AsyncGetWithJsoupMulti.AsyncResponse() {
            @Override
            public void processFinish(HashMap<String, Document> output, int position) {
                if (output == null) {
                    System.out.println("================= getMedicineUsageData: Output boş geldi");
                    return;
                }

                if (urlType == URLTypes.kt) {
                    List<String> titles = new ArrayList<>();
                    HashMap<String, List<String>> text = new HashMap<>();

                    for (Object outputEntryObject : output.entrySet()) {
                        Map.Entry entry = (Map.Entry) outputEntryObject;
                        String key = (String) entry.getKey();
                        Document value = (Document) entry.getValue();
                        Elements fullText = value.getElementsByClass("prospektus_text");
                        if (fullText != null) {
                            Element cleanVersionOfFullText = new Element("div");
                            Elements allChildrens = fullText.get(0).children();
                            for (Element textElement : allChildrens) {
                                if (!textElement.tagName().contains("script") && !textElement.tagName().contains("div")) {//bu tagları at
                                    cleanVersionOfFullText.append(textElement.toString());
                                }
                            }

                            List<String> subText = new ArrayList<>();
                            subText.add(cleanVersionOfFullText.html());
                            text.put(key, subText);
                            titles.add(key);
                            //System.out.println(value.html());
                        } else {
                            System.out.println("fullText IS EMPTY NULLLLLLLLLLLLLLLLLL");
                        }
                    }

                    if (titles.size() != 0 && text.size() != 0) {
                        CustomExpandableListAdapter customExAdapter = new CustomExpandableListAdapter(getApplicationContext(), titles, text);
                        mainActivity_expandableListMedicine.setAdapter(customExAdapter);
                    }

                } else if (urlType == URLTypes.prescription) {
                    List<String> titles = new ArrayList<>();
                    HashMap<String, List<String>> text = new HashMap<>();

                    for (Object outputEntryObject : output.entrySet()) {
                        Map.Entry entry = (Map.Entry) outputEntryObject;
                        String key = (String) entry.getKey();
                        Document value = (Document) entry.getValue();
                        Elements fullText = value.getElementsByClass("prospektus_text");
                        if (fullText != null) {
                            Element cleanVersionOfFullText = new Element("div");
                            Elements allChildrens = fullText.get(0).children();
                            for (Element textElement : allChildrens) {
                                if (!textElement.tagName().contains("script") && !textElement.tagName().contains("div")) {//bu tagları at
                                    cleanVersionOfFullText.append(textElement.toString());
                                }
                            }

                            List<String> subText = new ArrayList<>();
                            subText.add(cleanVersionOfFullText.html());
                            text.put(key, subText);
                            titles.add(key);
                            //System.out.println(value.html());
                        } else {
                            System.out.println("fullText IS EMPTY NULLLLLLLLLLLLLLLLLL");
                        }
                    }

                    if (titles.size() != 0 && text.size() != 0) {
                        CustomExpandableListAdapter customExAdapter = new CustomExpandableListAdapter(getApplicationContext(), titles, text);
                        mainActivity_expandableListMedicine.setAdapter(customExAdapter);
                    }
                }
            }
        };

        AsyncGetWithJsoupMulti asyncGetWithJsoupMulti = new AsyncGetWithJsoupMulti(asyncResponse);
        asyncGetWithJsoupMulti.execute(medicineUrlObject.getUrlData());//https://stackoverflow.com/questions/21132692/java-unchecked-unchecked-generic-array-creation-for-varargs-parameter

    }

    private void fillTheHomeList() {
        AsyncGetWithJsoup.AsyncResponse asyncResponse = new AsyncGetWithJsoup.AsyncResponse() {
            @Override
            public void processFinish(Document output, int position) {
                if (output == null) {
                    Toast.makeText(MainActivity.this, "Açılış Listei Boş Geldi", Toast.LENGTH_SHORT).show();
                    System.out.println("Açılış Listesi Boş Geldi");
                }
                Element encokArananlar = output.selectFirst(".encokaranlar");
                HashMap<String, String> nameAndUrlsHashMap = new HashMap<>();
                if (encokArananlar != null) {
                    Elements encokArnanlar_sutunlar = encokArananlar.select(".yeni_0");
                    for (Element sutun : encokArnanlar_sutunlar) {
                        Elements dataCells = sutun.select("div > table > tbody > tr > td");
                        for (Element dataCell : dataCells) {
                            Element link = dataCell.selectFirst("a");
                            String href = link.attr("href").replaceFirst("//", "");
                            String text = link.text();
                            nameAndUrlsHashMap.put(text, href);
                        }
                    }

                    setUrlsOfHomeScreen(nameAndUrlsHashMap);
                } else {
                    Toast.makeText(MainActivity.this, "En çok arananlar listesi çekilemedi", Toast.LENGTH_SHORT).show();
                }
            }
        };

        AsyncGetWithJsoup asyncGetWithJsoup = new AsyncGetWithJsoup(asyncResponse);
        asyncGetWithJsoup.execute(LinkLayer.URL_HOME);
    }

//------------------------------------------CODE SCANING PROCESS------------------------------------//

    //Scanning Process arrangements / Kod tarama işlemleri hazırlıkları ↓


    public void ScannerButtonClicked(View v) {
        IntentIntegrator integrator = new IntentIntegrator(this);
        //INFO → https://github.com/journeyapps/zxing-android-embedded/blob/master/zxing-android-embedded/src/com/google/zxing/integration/android/IntentIntegrator.java
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt(getString(R.string.scanning_is_on_process_title));
        integrator.setCameraId(0); //rear camera 0, ön kamera için 1
        integrator.setBeepEnabled(false); // ses çıkarmasın
        integrator.setBarcodeImageEnabled(false);//alınan resim kayıt yolu bilgisi sonuçlara dahil edilmesin.
        integrator.setCaptureActivity(CaptureActivityPortrait.class);//camera dikey açılsın diye/workaround for the portrait mode
        integrator.initiateScan();
    }

    //Taramadan Sonra Kameradan gelen sonuçların işlenmesi ↓
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, getString(R.string.scanning_cancelled), Toast.LENGTH_SHORT).show();
                return;
            }

            if (result.getFormatName().equals("DATA_MATRIX")) {
                //data matrix type has more than one fields in it so need to split.
                //bu iş düşündüğümden de karışıkmış. https://www.databar-barcode.info/application-identifiers/
                //https://www.mediclabel.nl/en/datamatrix/
                //sıralama:
                //A product identification code: GTIN (01)  => n2+n14
                //A serial number (21)  => n2+an..20
                //An expiry date (17)  => n2+n6
                //A batch number (10)  =>n2+an..20
                String content = result.getContents();
                int indexOf01 = content.indexOf("01");
                String serialNumb = content.substring(indexOf01 + 2, +indexOf01 + 2 + 14);
                int indexOfNonZeroStart = 0;
                for (int i = 0; i < serialNumb.length(); i++) {
                    if (serialNumb.charAt(i) == '0') {
                        continue;
                    } else {
                        indexOfNonZeroStart = i;
                        break;
                    }
                }

                serialNumb = serialNumb.substring(indexOfNonZeroStart, serialNumb.length());
                InputSearch.setQuery(serialNumb, true); // tarama sonucunu yaz ve ara
            } else {
                InputSearch.setQuery(result.getContents(), true); // tarama sonucunu yaz ve ara
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    //---------------------------------------------- AsyncTask ----------------------------------------------//
    private class MakeTheSearch extends AsyncTask<String, Void, Void> {
        ProgressBar prog = findViewById(R.id.ProBar); //yükleniyor diyalogu(yuvarlaklı) / on progress notification bar
        Document doc = null; //for the results / sonuçlar için bir nesne


        @Override
        protected void onPreExecute() {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE); //Ekranı kilitle, dokunmalar devre dışı / lock the screen, make it untouchable :)
            prog.setVisibility(View.VISIBLE); //show the progress bar / yükleniyor zımbırtısını göster.
        }

        @Override
        protected Void doInBackground(String... arg) {
            try {
                doc = Jsoup.connect(arg[0]).timeout(20000).get(); // adrese bağlan ve htmli al
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null; // bu ifade "onpostexecute" adresine yollanacak sonuçlar için(hemen altta). ama biz bunu kullanmıyoruz. birazcık medeniyetsiz gibi davranıp kendi bildiğimiz yoldan gittik, kullanacığımız nesneyi yukarıda, sınıf içinde tanımladık(document nesnesi)
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE); //ekran dokunmalar aktif
            prog.setVisibility(View.GONE);//progressbarı kapat
            if (doc != null) {//dosya boş değilse / bilgi alınmışsa işlemleri yap
                String urlResult = ""; //sonuçlar için bir string
                //urlResult = doc.select("div.r a:has(h3)").attr("href").toString(); //arama sonucundaki ilk linki al. bunun için örnek bir google aramasının html sayfası incelendi. h3 başlıklarının(gördüğümüz büyük mavi yazılar) içindeki url tagı(a)'nın url blgisini içeren 'href' etiketinin içeriğini al. dedik.
                //↑ bu kısım iptal edildi ↓ bu kısım çalışacak
                //-----------------------------------------Daha düzgün bir url parse için Ekleme:
                int size = doc.select("div.r a:has(h3)").size();
                for (int i = 0; i < size; i++) {
                    Element current;
                    current = doc.select("div.r a:has(h3)").get(i);
                    if (current.attr("href").contains("/v/")) {//ilaç bilgileri olan url adresleri bu kısmı içeriyor. ona göre url seçeceğiz.
                        System.out.println("if içerisi çalıştı i=" + i);
                        urlResult = current.attr("href");
                        break;
                    }

                }

                if (urlResult == "") {//eğer sonuç yoksa bulunamadı de, geri dön. // if there is no result return back with informational text.
                    textview_warning_messages.setText(getString(R.string.label_couldnt_find));
                    clearTheResultFields();
                    return;
                }

                String[] cutted_url = urlResult.split("/"); // split the url in order to get the main mpage of medicine. / ilacın ana sayfasına gitmek için url'i ayrıştır
                urlResult = cutted_url[0] + "/" + cutted_url[1] + "/" + cutted_url[2] + "/" + cutted_url[3] + "/" + cutted_url[4]; // yeni urli kaydet. / save the new url
                textview_warning_messages.setText(getString(R.string.label_search_completed_text));//arama bitti bilgisini ver.
                getMedicineResultsAndShowDetails(urlResult);


//                GetThePage gp = new GetThePage(); // ilaç sayfası için yeni bir asynctask hazırla
//                gp.execute(urlResult);//ve başlat.
            } else { //dosya boşsa
                textview_warning_messages.setText(getString(R.string.label_connection_weak_text)); // bağlantı zayıf bilgisini ver.
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_actvity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuitem_ayarlar: {
                Intent intent = new Intent(MainActivity.this, Settings.class);
                startActivity(intent);
                return true;
            }
        }

        return true;
    }

    private void CloseTheKeyboard() { // Klavyeyi Kapat
        View vi = this.getCurrentFocus();
        if (vi != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(vi.getWindowToken(), 0);
        }
    }
}


//--------------------------------------------------------------------------------------------------
//                                          PROJE GÜNLÜKLERİ
//--------------------------------------------------------------------------------------------------

/*

20.07.18 cuma
Jsoup testi için internet bağlantısı gerekli. onun için de ayrı bir thread'de işlem yapmak lazım.
öyle yapınca da bu sefer ana thread işlemeye devam ederken internet işini yapan thread beklenemediği için
arka planda henüz internetten çekilmemiş veri üzerinde işlem yapılmaya çalışılıyor.
hal böyle iken de ana threadin bir şekilde veri çekildikten sonra işlemlerine devam etmesi lazım.
bunun çözümü için uğraşıyorum iki gündür. kafayı yememek mümkün değil.
internetteki tüm jsoup örnekleri her birr işlem için ayrı ayrı async yazıp bunların sonuçlarını
UI'e aktarma üzerine. oysa ben bir kez internetle işimi bitirip sonrasında gelen veri üzerinde kesme/biçme manipüle işlemlerini
yapmak istiyorum. allam çok mu şey istedim! :)
her neyse, internette(stackoverflow tabii ki) async task cevabını mainclass'a yollamak için bir arayüz(interface)
olulturma metodu üzerinde durulmuş[1]. bu metodu deneyeceğim. tabi bu metodu bulana kadar yapmadığım deneme/test felan kalmadı.
bir thread nasıl davranır felan baya müptelası olduk çıktık işin. threadi beklediğimde
aslındaaa.......wait a minute :) son bir şey daha deneyeceğim.

Hmmmm... Evvet. aklımdaki şeyi denedim ve yine olmadı. uzunca bir süre mainthread(anathread)'in,
çalıştırdığım internet threadinin bitmesini beklemesini sağlamaya çalışmakla uğraştım. sonrasında
yıllar önce thread konusunda öğrendiğimiz join metodunu bir şekilde buldum/hatırladım. ve hemen denemey başladım
lakin o işler hiç ötle olmuyor nedense. android anlmsız bir şekilde thread start verilmeden önceki komutları işlemeye
fırsat bulamıyor ya işlemiyor. yapmaya çalıştığım şey basit. sırasıyla:
-ekrana bir yükleniyor 'progress dialog'u ekle
-internetten veri çekecek threadi çalıştır
-threadin bitmesini bekle
-kaldığın yerden devam et...
böyle yapınca, ekranda yükleniyor diyaloğu çıkmaya fırsat bulamadan ekran bir süreliğine donuyor.
ardından da sonuçlar(şansımıza program çökmemişse) ekrana geliyor. bu oldukça yavan/berbat bir kodlama şekli.
sorun tam olarak da bu aslında. bu sorunun üstesinden gelemey çalışıyorum. thread örneğini tekrar deneyip bbaşarısız olduktan sonra
async task işlemine devam edeceğim. muhtemelen join işlemi bu işin baş sorumlusu ama zaten onu yapmayınca daha beterleri i
ile karşılaşıyoruz. kısacası ana threadi incitmeden bekletmenin bir yolunu bulmam lazım ki
yanthread-internetthreadi- işini bitirmeye vakit bulabilsin.

--------------------------------------------------------
not:
handler, runonUiThread gibi opsiyonları felan da denedim hepsinde sonuç hüsran... bu uygulamalar da
arayüzü yeterince hızlı güncelleyemiyor veya düzgün tepki vermiyorlar. üstelik ana program kodları
işlemye devam ettiği için ve tüm bu heyulada thread işleminin hemen ardından gelen satırda, thread
işi bittikten sonra gelecek veriyi(document değişkeni) işlemye çalıştığım için yine hata alıyorum.
while döngüsü ile beklemeye çalışmak da yine çok da iç açıcı olmayan hezeyan sonuçlar veriyor ki
zaten uzun kısırdöngü veya "wait()" gibi komutların da yine thread içinde verilmesi gerektiği
android eğitimlerinde ve developer sayfalarında bahsedilen bir şey. nasıl bir mantıkla bütün
programı bir main thread üzerne inşa etmişler onu da merak ediyorum doğrusu. acısını bizden
çıkarmışlar resmen. donanım kibar olunca acısı yazılımdan çıkıyor anlaşılan. tasarım bunu ispatlıyor :/


--------------------------------------------------------
ohhh nihayet...async task ile denilen işlemi yaptım ve sonuçları async task dışında işlemeyi başardım...
oh çok şükğr :)

henüz erken konuşmuşum. sadece async task postprocess işlemini class dışına taşımışız. ama main thread
işlemeye devam ediyor. belki de istediğim şey program mantığına aykırı/absürd bir şeydir diye
düşünmeye başladım. saçma bir şey yapmaya çalışıyorum sanırım. neyse şimdilik bu kadarı yeterli. ilerde başımıza
sorun açacağını sanmıyorum.


--------------------------------------------------------
android beni her seferinde korkutmayı başarıyor bravo!!
şimdi de öğrend,m ki bir asynctask sadece ve sadece bir kez yürütülebiliniyormuş...korkunç bir şey!!
bunu da artık nasıl çözeceğimi cidden bilmiyorum. kesinlikle bu işte bir terslik var ya da cidden çok
yanlış bir yoldayım galiba, herhalde yani galiba.....:/

--------------------------------------------------------
httpurlconnection nesnesi ile denemeler yapıyorum. ama kafam ağrıyor, baş belası yazılımlar.
çok yanlışım var çok!! veri çekmenin alternatiflerini arıyorum şu an. sadi hocanın bir videosu var
onu indirmeye çalışıyorum. internet de yavaş bayağğı....offf allam!!!!!!!

--------------------------------------------------------
oldukça uzun süre sancılı bir araştırmadan ve tonla okumadan sonra aldığım bir kaç karar ve en sonunda
geldiğim noktayı paylaşmak istiyorum:
ilk önce afalladım. kabul etmem gerek. nasıl olur da asynctask gibi bir işlem bir kez tek çağrılabi,lirdi?
nedenini iliyordum aslında ama geldiğim noktada biraz absürd duruyordu çünkü.
her neyse. bir bilene sor kaidesini uygulamaya çalıştım ve sorup soruşturdum. tabii ki her zaman olduğu
gibi adam akıllı yaraya merhem sürecek bir babayiğit bulunamadı :) uğraştığım şeyleri ne kadar çok istesem de
başkalarının yardımı ile çözemedim bugüne kadar. en azından hiç kısmet olmadı. sanırım kader :)
her neyse. sonra izlediğim videoları gezinmeye başladım. arka plan işlemleri için service'ler kullanılır,
denilmişti bir yerde. bir an durup. hakkaten ya neden bir servis yazmıyorum ki? diye düşündüm.
bu arada sorunumu danıştığım kim olduysa sürekli android de native dille uğraşma denildi, başa dert denildi.
haksız da sayılmazlar cidden uğraştırıyor insanı ama direk hazır framework alıp kullanmayı da beceremedim hiç,
o yüzden zor da olsa bildiğim yoldan devam edeceğim sanırım.

tüm bunlar olurken ulan dedim acaba şu asynctask objesini buton metodunda tanımlayıp execute etsem nasıl
olur diye düşündüm ve denedim. ve sonuç: hata yok!
butona her bastığımda tekrar tekrar bir nesne oluşturduğum için, sıkıntı olmuyor. yine kaldığım yere dönmek için
günlerce yol yürümüşüm anlayacağınız. yok böyle bir tecrübe :)


--------------------------------------------------------
async task problemini çözünce gerisi geldi çok şükür. şimdilik iyi gidiyor. iki tane birbirini
ateşleyen async task görevim var. buton birini ateşleyince zincirleme gidiliyor. tabi bu çok etkili
bir şey değil. baya baş ağrıtabilir. ama şimdilik işe yarıyor.
ilk async task google araması yapıp site linkini alıyor, ikinci asynctask ise bu site linkindeki
veriyi çekip işliyor ve ilaç bilgisi ekrana geliyor.
tabi site oldukça karmaşık ve sonuçlar çok farklı olabiliyor. bu yüzden her şarta uyum sağlayabilen
bir yapıya gelene kadar daha çok yolumuz var. şu ana kadar sadece buton ile denemeler yaptım.
kamera için denemeler yapmadım. muhtemelen sadece sonuç kısmında asynctask ateşlemem yeterli olacak
ama henüz denemedik tabi.
--------------------------------------------------------
sonuçları göstermek için ikinci bir activity yazmıştım. o activity'e bilgi gönderme işlemi şu an
için başarılı. dediğim gibi ilacrehberi.com'un düzenini iyice anliz edip her şartta çalışan bir yol
bulmam lazım şimdilik. umarım çok zorlanmam. tabi uygulamayı biraz renklendirmek gerekiyor ki en
sevmediğim iştir, yapmayı da hiç beceremem. umarım güzel bir hava katabiliriz uygulamaya :)

--------------------------------------------------------
ilaç bilgisi görüntülendikten sonra geri tuşu ile yapılan geri dönüşlerde ekranın tekrar eski haline
dondürülmesi işlemi yağpıldı. ekrandaki fazlalıkalar temizlendi

edittext içerisinde metin silme işareti olan x işareti olmadığından searchview ile değiştirldi.

strings xml dosyası için düzenlenmeler yapıldı.
--------------------------------------------------------
program içerisinde türkçe ve ingilizce geçişler yapmak zor oluyormuş. bunu en sonda yapmayı planlıyorum.
şimdilik çalışır halde bir tr bir de en hazırlanmış string dosyalarımız var. telefon dili
değiştirildiğinde uygulama dili de otomatik değişiyor.
--------------------------------------------------------
programda düzenlemeler yapıldı. yorum satırları/açıklamalar eklendi.
ilacrehberi sitesinin yapısı daha detaylı incelendi, google arama sonuçları içerisinden çıkan ilk
sonuç linki parçalanıp tekrar birleştirilerek ilacın ana sayfasına giden link elde edildi. böylece
ilacrehberi sitesinin karmaşasına da bir çözüm getirilmiş olundu. ilacın ana sayfasına gidilerek ilaç
hakkındaki tüm teknik ve kullanım bilgilerine ulaşmak artık mümkün.

ilacrehberim sitesinin prospektüs ve kullanım bilgisi sayfalarındaki sekmelerin her biri ayrı bir
sayfada açıldığından ve bu datayı birleştirip tek bir sayfada göstermek için  en azından on-on beş tane
sayfadan veri çekilmesi gerektiğinden, ilaca dair bilgiyi derleyip tek bir edittext veya webview içerisinde
gösterme hayallerim suya düştü. bunun yerine ilaç ana sayfasında ilaç bilgisi içeren <table> tagları
çekilip webview objesine aktarıldı. kullanıcı bu sayfadan isterse kullanım bilgisini veya varsa prospektüsü
tıklayıp kalan işlemlerini tarayıcıdan halledebilir.
ayrıca bu table tagları programa entegre etmeyi düşündüğüm "ilaç eşdeğerleri" tablosunu da böylece
direkt eklemiş olduk. fena olmadı gibi :)

--------------------------------------------------------
telefon yan çevrilince düzgün görünmeyen öğeler düzenlendi, tasarımları düzeltildi.

ilaç bilgisi tablolarının en üstünde görünen fazlalık iconlar kaldırıldı. sadece ilk üç tanesi(kullanım bilgisi, prospektüs ve kullanma talimatı) butonları yerinde bırakıldı
--------------------------------------------------------
staj sonrası geliştirmeler:
2018.11.20
karekod okuma yöntemi değiştirldi, result objesinin sadece content'i(içeriği/değeri) alındı
2018.11.22
sitenin yapısındaki farklılıktan dolayı bazı arama sonuçları yeterince iyi parse edilemediği için sonuç linklerinin incelenme şekli değiştirildi.
ilaç bilgilerini içeren linki elde etmek için ilk sonuç yerine anahtar ifade olan "/v/" ibaresi bulunan sonuçlar seçilerek parse edilecek.
2018.11.26.22.33
result activity ekranına 3dot menü eklendi. 3dot menü özelliği, her cihazda aynı şekilde çalışmıyormuş meğer.
benim cihazımda(s3) fiziksel menü butonu olduğundan menü üst tarafta 3dot şeklinde değil de, altta gorünüyordu.
iki saattir emülatörde çalıştığı halde neden telefonda çalışmıyor diye kafa patlatıyordum sebebi buymuş meğer.
bu problemi stackoverflowdaki(https://stackoverflow.com/questions/19741319/android-action-bar-three-dots-not-displayed)
bir "workaround" ile yani bir nevi sorunun çevresinden dolaşarak hallettik çok şükür.
şu an her lşey stabil ve temiz.






[1] https://stackoverflow.com/questions/12575068/how-to-get-the-result-of-onpostexecute-to-main-activity-because-asynctask-is-a
*/


//adres örneği :https://www.google.com/search?q=site:https://www.ilacrehberi.com/%20katarin