package API;

import API.DB.GetDataFromDB;
import API.Data.DataCatalogs;
import API.Data.DataDocs;
import API.Data.DataKladr;
import API.Data.DataOIV;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ControllerSpring {

    private GetDataFromDB getData = new GetDataFromDB();

    @RequestMapping(value = "/getMenu", method = RequestMethod.GET)
    private List<DataCatalogs> getMenu() {
        return getData.getMenu();
    }

    @RequestMapping(value = "/getCountPages/page={page}", method = RequestMethod.GET)
    public Integer getCountPages(@PathVariable("page") String page) {
        return getData.getCountPages(page);
    }

    @RequestMapping(value = "/kladr/pagin={pagin}", method = RequestMethod.GET)
    public List<DataKladr> kladrDataPagin(@PathVariable("pagin") int pagin) throws Exception {
        if (pagin > 0)
            return getData.getKladr(pagin, 0, "", null);
        else throw new Exception("Не правильно задана страница, номер старницы должна быть > 0 ");
    }

    @RequestMapping(value = "/kladr/column={col}&sort={type}&likeText={likeText}&pagin={pagin}", method = RequestMethod.GET)
    public List<DataKladr> kladrSort(@PathVariable("col") int col,
                                     @PathVariable("type") String type,
                                     @PathVariable("pagin") int pagin,
                                     @PathVariable("likeText") String likeText) throws Exception {
        if (pagin > 0)
            return getData.getKladr(pagin, col, type.toUpperCase(), likeText);
        else throw new Exception("Не правильно задана страница, номер старницы должна быть > 0 ");
    }

    @RequestMapping(value = "/docs/pagin={pagin}", method = RequestMethod.GET)
    public List<DataDocs> docs(@PathVariable("pagin") int pagin) throws Exception {
        if (pagin > 0)
            return getData.getDocs(pagin, 0, "", null);
        else throw new Exception("Не правильно задана страница, номер старницы должна быть > 0 ");
    }

    @RequestMapping(value = "/docs/column={col}&sort={type}&likeText={likeText}&pagin={pagin}", method = RequestMethod.GET)
    public List<DataDocs> docsSort(@PathVariable("col") int col,
                                   @PathVariable("type") String type,
                                   @PathVariable("pagin") int pagin,
                                   @PathVariable("likeText") String likeText) throws Exception {
        if (pagin > 0)
            return getData.getDocs(pagin, col, type.toUpperCase(), likeText);
        else throw new Exception("Не правильно задана страница, номер старницы должна быть > 0 ");
    }

    @RequestMapping(value = "/oiv/pagin={pagin}", method = RequestMethod.GET)
    public List<DataOIV> oiv(@PathVariable("pagin") int pagin) throws Exception {
        if (pagin > 0)
            return getData.getOiv(pagin, 0, "", null);
        else throw new Exception("Не правильно задана страница, номер старницы должна быть > 0 ");
    }

    @RequestMapping(value = "/oiv/column={col}&sort={type}&likeText={likeText}&pagin={pagin}", method = RequestMethod.GET)
    public List<DataOIV> oivSort(@PathVariable("col") int col,
                                 @PathVariable("type") String type,
                                 @PathVariable("pagin") int pagin,
                                 @PathVariable("likeText") String likeText) throws Exception {
        if (pagin > 0)
            return getData.getOiv(pagin, col, type.toUpperCase(), likeText);
        else throw new Exception("Не правильно задана страница, номер старницы должна быть > 0 ");
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