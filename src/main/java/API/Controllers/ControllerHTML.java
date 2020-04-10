package API.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;


@Controller
public class ControllerHTML {

    @RequestMapping(value="/add", method=RequestMethod.GET)
    public String greeting(@RequestParam(name="name", required=false, defaultValue="World") String name, Model model) {
        model.addAttribute("name", name);
        return "addDocs";
    }

    @RequestMapping(value = "/add", method= RequestMethod.POST)
    public @ResponseBody
    String uploadDocs(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes){
        if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();
                BufferedOutputStream stream =
                        new BufferedOutputStream(new FileOutputStream(new File(file.getName())));
                stream.write(bytes);
                stream.close();
                return "Вы удачно загрузили ";
            } catch (Exception e) {
                return "Вам не удалось загрузить ";
            }
        } else {
            return "Вам не удалось загрузить ";
        }
    }
}
