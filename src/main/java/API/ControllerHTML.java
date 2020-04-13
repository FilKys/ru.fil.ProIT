package API;

import API.Data.DataDocs;
import API.Data.DataKladr;
import API.Data.DataOIV;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.List;
import java.util.Objects;


@Controller
public class ControllerHTML {

    private Database db = new Database();

    @RequestMapping(value = "/kladr", method = RequestMethod.GET)
    public String kladr(@RequestParam(name = "name", required = false, defaultValue = "World") String name, Model model) {
        List<DataKladr> dataKladrList = db.getDataKladr();
        model.addAttribute("kladrCat", dataKladrList);
        return "kladr";
    }

    @RequestMapping(value = "/docs", method = RequestMethod.GET)
    public String docs(@RequestParam(name = "name", required = false, defaultValue = "World") String name, Model model) {
        List<DataDocs> dataDocsList = db.getDataDocs();
        model.addAttribute("docsCat", dataDocsList);
        return "docs";
    }

    @RequestMapping(value = "/oiv", method = RequestMethod.GET)
    public String oiv(@RequestParam(name = "name", required = false) String name, Model model) {
        List<DataOIV> dataOIVList = db.getDataOIV();
        model.addAttribute("oivCat", dataOIVList);
        return "oiv";
    }

    @RequestMapping(value = "/kladrAdd", method = RequestMethod.POST)
    public @ResponseBody
    String uploadDocs(@RequestParam("file") MultipartFile file, Model model) {
        File fileIn = new File(Objects.requireNonNull(file.getOriginalFilename()));
        String upload = "";
        if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();
                BufferedOutputStream stream =
                        new BufferedOutputStream(new FileOutputStream(fileIn));
                stream.write(bytes);
                stream.close();
                db.addInDB(fileIn, "kladr");
                fileIn.delete();
                upload = "Файл загружен";

            } catch (Exception e) {
                upload = "Вам не удалось загрузить файл";
            }
        } else {
            upload = "Вам не удалось загрузить файл";
        }
        model.addAttribute("uploadFile", upload);
//        return "redirect:/kladr";
        return "kladr";
    }
}
