package API;

import API.Data.DataCatalogs;
import API.Data.DataDocs;
import API.Data.DataKladr;
import API.Data.DataOIV;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.List;

@EnableSwagger2
@RestController
public class ControllerSpring {

    private Database db = new Database();
    private List<DataCatalogs> dataCatalogsList;

    @RequestMapping(value = "/getMenu", method = RequestMethod.GET)
    private List<DataCatalogs> getMenu() {
        this.dataCatalogsList = db.getMenu();
        return dataCatalogsList;
    }

    @RequestMapping(value = "/getCountPages/page={page}", method = RequestMethod.GET)
    public Integer getCountPages(@PathVariable("page") String page) {
        return db.getPaginator(page);
    }

    @RequestMapping(value = "/kladr/pagin={pagin}", method = RequestMethod.GET)
    public List<DataKladr> kladrDataPagin(@PathVariable("pagin") int pagin) {
        return db.getKladr(pagin-1, 0, "", null);
    }

    @RequestMapping(value = "/kladr/column={col}&sort={type}&likeText={likeText}&pagin={pagin}", method = RequestMethod.GET)
    public List<DataKladr> kladrSort(@PathVariable("col") int col,
                                     @PathVariable("type") String type,
                                     @PathVariable("pagin") int pagin,
                                     @PathVariable("likeText") String likeText) {
        return db.getKladr(pagin, col, type.toUpperCase(), likeText);
    }

    @RequestMapping(value = "/docs/pagin={pagin}", method = RequestMethod.GET)
    public List<DataDocs> docs(@PathVariable("pagin") int pagin) {
        return db.getDocs(pagin, 0, "", null);
    }

    @RequestMapping(value = "/docs/column={col}&sort={type}&likeText={likeText}&pagin={pagin}", method = RequestMethod.GET)
    public List<DataDocs> docsSort(@PathVariable("col") int col,
                                   @PathVariable("type") String type,
                                   @PathVariable("pagin") int pagin,
                                   @PathVariable("likeText") String likeText) {
        return db.getDocs(pagin, col, type.toUpperCase(), likeText);
    }

    @RequestMapping(value = "/oiv/pagin={pagin}", method = RequestMethod.GET)
    public List<DataOIV> oiv(@PathVariable("pagin") int pagin) {
        return db.getOiv(pagin, 0, "",null);
    }

    ////TODO Настроить сортировку
    @RequestMapping(value = "/oiv/column={col}&sort={type}&likeText={likeText}&pagin={pagin}", method = RequestMethod.GET)
    public List<DataOIV> oivSort(@PathVariable("col") int col,
                                 @PathVariable("type") String type,
                                 @PathVariable("pagin") int pagin,
                                 @PathVariable("likeText") String likeText) {
        return db.getOiv(pagin, col, type.toUpperCase(), likeText);
    }
//
//    @RequestMapping(value = "/kladrAdd", method = RequestMethod.POST)
//    public @ResponseBody
//    String uploadDocs(@RequestParam("file") MultipartFile file, Model model) {
//        File fileIn = new File(Objects.requireNonNull(file.getOriginalFilename()));
//        String upload = "";
//        if (!file.isEmpty()) {
//            try {
//                byte[] bytes = file.getBytes();
//                BufferedOutputStream stream =
//                        new BufferedOutputStream(new FileOutputStream(fileIn));
//                stream.write(bytes);
//                stream.close();
//                db.addInDB(fileIn, "kladr");
//                fileIn.delete();
//                upload = "Файл загружен";
//
//            } catch (Exception e) {
//                upload = "Вам не удалось загрузить файл";
//            }
//        } else {
//            upload = "Вам не удалось загрузить файл";
//        }
//        model.addAttribute("uploadFile", upload);
////        return "redirect:/kladr";
//        return "kladr";
//    }

}
