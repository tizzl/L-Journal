package com.timo.Learning_Journal.controller;


import com.timo.Learning_Journal.entity.Person;
import com.timo.Learning_Journal.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class PersonController {
    @Autowired
    private PersonService personService;

    @GetMapping("/person/{id}")
    public String viewPerson(@PathVariable Long id, Model model) {
        // Person aus der DB holen
        Person person = personService.findById(id).orElse(null);

        if (person == null) {
            return "redirect:/entries"; // fallback, falls Person nicht existiert
        }
        model.addAttribute("person", person);
        return "person"; // person.html muss existieren
    }
}
