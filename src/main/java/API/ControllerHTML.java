package API;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.*;
import java.util.Objects;


@Controller
public class ControllerHTML {

    @RequestMapping(value="/kladr", method=RequestMethod.GET)
    public String kladr(@RequestParam(name="name", required=false, defaultValue="World") String name, Model model) {
        model.addAttribute("name", name);
        return "kladr";
    }

    @RequestMapping(value = "/kladrAdd", method= RequestMethod.POST)
    public @ResponseBody
    String uploadDocs(@RequestParam("file") MultipartFile file, Model model){
        Database db = new Database();
        File fileIn = new File(Objects.requireNonNull(file.getOriginalFilename()));
        if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();
                BufferedOutputStream stream =
                        new BufferedOutputStream(new FileOutputStream(fileIn));
                stream.write(bytes);
                stream.close();
//                db.addInDB(fileIn,"kladr");
                fileIn.delete();
                return "Вы удачно загрузили файл";
            } catch (Exception e) {
                return "Вам не удалось загрузить файл";
            }
        } else {
            return "Вам не удалось загрузить файл";
        }
    }
}
