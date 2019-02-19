package com.example.simone.maria;

import android.Manifest;
import android.app.UiModeManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;
import com.snatik.storage.Storage;

import org.zeroturnaround.zip.ZipUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

import static android.app.UiModeManager.MODE_NIGHT_NO;

public class About extends AppCompatActivity {

    private final DatabaseHelper db = new DatabaseHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Disattivo la night mode altrimenti il file picker fa le icone sbagliate
        UiModeManager uiModeManager = (UiModeManager) this.getSystemService(Context.UI_MODE_SERVICE);
        uiModeManager.setNightMode(MODE_NIGHT_NO);
        Element versionElement = new Element();
        versionElement.setTitle(getString(R.string.version_text));
        Element export = new Element();
        export.setIconDrawable(R.drawable.ic_file_export_solid);
        export.setTitle(getString(R.string.export_zip));
        export.setOnClickListener((View view) -> exportRicette());
        Element importer = new Element();
        importer.setIconDrawable(R.drawable.ic_file_export_solid);
        importer.setTitle(getString(R.string.import_zip));
        importer.setOnClickListener((View view) -> importRicette());
        View aboutPage = new AboutPage(this)
                .isRTL(false)
                .setDescription(getString(R.string.about_desc))
                .setImage(R.drawable.ic_grandmother)
                .addItem(versionElement)
                .addItem(export)
                .addItem(importer)
                .addGroup(getString(R.string.contact_me))
                .addEmail(getString(R.string.my_email))
                .addWebsite(getString(R.string.my_website))
                .addFacebook(getString(R.string.my_facebook))
                .addTwitter(getString(R.string.my_twitter))
                .addInstagram(getString(R.string.my_instagram))
                .create();
        setContentView(aboutPage);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }


    private void exportRicette() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
        Storage storage = new Storage(getApplicationContext());
        Date currentTime = Calendar.getInstance().getTime();
        String path = storage.getExternalStorageDirectory() + File.separator + getString(R.string.export_folder) + currentTime.toString();
        ArrayList<Ricetta> ricette = db.getAllRicette();
        for (int i = 0; i < ricette.size(); i++) {
            Ricetta ricetta = ricette.get(i);
            String newDir = path + File.separator + ricetta.getName();
            storage.createDirectory(newDir);
            String textRicetta = ricetta.getName() + "\n";
            textRicetta = textRicetta + ricetta.getmDescription() + "\n" + ricetta.getPeople() + "\n" + ricetta.getCalories() + "\n" + getString(R.string.ingredienti) + "\n";
            ArrayList<Ingrediente> ingredienti = db.getIngredientiFromRicetta(ricetta);
            for (int j = 0; j < ingredienti.size(); j++) {
                Ingrediente ingrediente = ingredienti.get(j);
                StringBuilder sb = new StringBuilder();
                textRicetta = sb.append(textRicetta).append(ingrediente.getName()).append(" - ").append((ingrediente.getGrams().toString())).append("\n").toString();
            }
            textRicetta = textRicetta + getString(R.string.tags) + "\n";
            ArrayList<Tag> tags = db.getTagsFromRicetta(ricetta);
            for (int k = 0; k < tags.size(); k++) {
                Tag tag = tags.get(k);
                StringBuilder sb = new StringBuilder();
                textRicetta = sb.append(textRicetta).append(tag.getTagName()).append("\n").toString();
            }
            textRicetta = textRicetta + getString(R.string.passi) + "\n";
            ArrayList<Passo> passi = db.getPassiFromRicetta(ricetta);
            for (int l = 0; l < passi.size(); l++) {
                Passo passo = passi.get(l);
                StringBuilder sb = new StringBuilder();
                textRicetta = sb.append(textRicetta).append(Integer.toString(l + 1)).append(" - ").append(passo.getDescription()).append("\n").toString();
            }
            storage.createFile(newDir + File.separator + getString(R.string.ricetta_filename), textRicetta);
            ArrayList<Immagine> immagini = db.getImmaginiFromRicetta(ricetta);
            for (int s = 0; s < immagini.size(); s++) {
                Immagine img = immagini.get(s);
                String uri = img.getPhoto_uri().toString();
                String filename = uri.substring(uri.lastIndexOf("/") + 1);
                String fileUri = uri.substring(7);
                fileUri = fileUri.replace("%20", " ");
                storage.copy(fileUri, newDir + File.separator + ricetta.getId() + ">>" + filename);
            }

        }
        if (ricette.size() != 0) {
            ZipUtil.pack(new File(path), new File(path + ".zip"));
            storage.deleteDirectory(path);
            Toast.makeText(this, getString(R.string.export_success), Toast.LENGTH_LONG).show();

        } else {
            Toast.makeText(this, getString(R.string.export_fail), Toast.LENGTH_LONG).show();
        }
    }


    private void importRicette() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
        new MaterialFilePicker()
                .withActivity(this)
                .withRequestCode(1)
                .withFilter(Pattern.compile(".*\\.zip$")) // Filtering files and directories by file name using regexp
                .withFilterDirectories(false) // Set directories filterable (false by default)
                .withHiddenFiles(true) // Show hidden files and folders
                .start();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onDestroy() {
            db.closeDB();
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) {
            String path = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);
            Storage storage = new Storage(getApplicationContext());
            path = path.substring(0, path.lastIndexOf('.'));
            ZipUtil.unpack(new File(path + ".zip"), new File(path));
            List<File> files = storage.getNestedFiles(path);
            //Prima creo le ricette
            for (int i = 0; i < files.size(); i++) {
                Log.e("FILE", files.get(i).toString());
                String u = files.get(i).getAbsolutePath();
                String fileName = u.substring(u.lastIndexOf('/') + 1);
                String extension = fileName.substring(fileName.lastIndexOf('.') + 1);
                if (extension.equals("txt")) {
                    String content = storage.readTextFile(u);
                    String contents[] = content.split("\n");
                    String ricettaName = contents[0];
                    String ricettaDesc = contents[1];
                    Integer ricettaPeople = Integer.parseInt(contents[2]);
                    Integer ricettaCal = Integer.parseInt(contents[3]);
                    if (ricettaCal == 0) {
                        ricettaCal = null;
                    }
                    int j = 5;
                    Ricetta r = new Ricetta(ricettaName, db.getMaxRicetta() + 1, ricettaDesc, ricettaPeople, ricettaCal);
                    db.createRicetta(r);
                    while (!(contents[j].equals("%%TAGS%%"))) {
                        String ingredientStrings[] = contents[j].split(" - ");
                        String ingredientName = ingredientStrings[0];
                        Integer ingredientQty = Integer.parseInt(ingredientStrings[1]);
                        db.createIngrediente(new Ingrediente(db.getMaxIngrediente() + 1, ingredientName, ingredientQty, r.getId()));
                        j++;
                    }
                    j++;
                    while (!(contents[j].equals("%%PASSI%%"))) {
                        String tagName = contents[j];
                        db.createTag(new Tag(db.getMaxTag() + 1, tagName, r.getId()));
                        j++;
                    }
                    j++;
                    while (j < contents.length) {
                        String passo[] = contents[j].split(" - ");
                        Integer passoNumber = Integer.parseInt(passo[0]);
                        String passoDesc = passo[1];

                        db.createPasso(new Passo(db.getMaxPasso() + 1, passoNumber, r.getId(), passoDesc, 0));
                        j++;
                    }
                    List<File> pics = storage.getNestedFiles(path + File.separator + r.getName());
                    for (int k = 0; k < pics.size(); k++) {
                        String picUri = pics.get(k).getAbsolutePath();
                        String picFileName = picUri.substring(picUri.lastIndexOf('/') + 1);
                        String picExtension = picFileName.substring(picFileName.lastIndexOf('.') + 1);
                        picFileName = picFileName.substring(0, picFileName.lastIndexOf('.'));
                        String photoPath = "";
                        File photoDir = getBaseContext().getExternalFilesDir("Photos");
                        if (photoDir != null) {
                            photoPath = photoDir.getAbsolutePath();
                        }
                        if (!(picExtension.equals("txt"))) {
                            String newPath = photoPath + File.separator + picFileName + "." + picExtension;
                            storage.copy(picUri, newPath);
                            db.createImmagine(new Immagine(db.getMaxImmagine() + 1, Uri.parse(newPath), r.getId()));
                        }
                    }
                }
            }
            Toast.makeText(this, getString(R.string.import_success), Toast.LENGTH_LONG).show();
        }
    }

}
