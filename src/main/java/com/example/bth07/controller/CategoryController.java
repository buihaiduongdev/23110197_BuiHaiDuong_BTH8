package com.example.bth07.controller;

import com.example.bth07.entity.Category;
import com.example.bth07.entity.User;
import com.example.bth07.service.CategoryService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    private boolean isUserAuthorized(HttpSession session) {
        User currentUser = (User) session.getAttribute("currentUser");
        return currentUser != null;
    }

    private void addUserRolesToModel(HttpSession session, Model model) {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser != null) {
            boolean isAdminOrManager = currentUser.getRole() == User.Role.admin || currentUser.getRole() == User.Role.manager;
            model.addAttribute("isAdminOrManager", isAdminOrManager);
        }
    }

    @GetMapping
    public String listCategories(Model model, HttpSession session) {
        if (!isUserAuthorized(session)) {
            return "redirect:/login";
        }
        model.addAttribute("categories", categoryService.findAll());
        addUserRolesToModel(session, model);
        return "category/list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model, HttpSession session) {
        if (!isUserAuthorized(session)) {
            return "redirect:/login";
        }
        model.addAttribute("category", new Category());
        addUserRolesToModel(session, model);
        return "category/add";
    }

    @PostMapping("/add")
    public String addCategory(@ModelAttribute("category") Category category, HttpSession session) {
        if (!isUserAuthorized(session)) {
            return "redirect:/login";
        }
        categoryService.save(category);
        return "redirect:/category";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Integer id, Model model, HttpSession session) {
        if (!isUserAuthorized(session)) {
            return "redirect:/login";
        }
        model.addAttribute("category", categoryService.findById(id));
        addUserRolesToModel(session, model);
        return "category/edit";
    }

    @PostMapping("/edit")
    public String editCategory(@ModelAttribute("category") Category category, HttpSession session) {
        if (!isUserAuthorized(session)) {
            return "redirect:/login";
        }
        categoryService.save(category);
        return "redirect:/category";
    }

    @GetMapping("/delete/{id}")
    public String deleteCategory(@PathVariable("id") Integer id, HttpSession session) {
        if (!isUserAuthorized(session)) {
            return "redirect:/login";
        }
        categoryService.deleteById(id);
        return "redirect:/category";
    }
}
