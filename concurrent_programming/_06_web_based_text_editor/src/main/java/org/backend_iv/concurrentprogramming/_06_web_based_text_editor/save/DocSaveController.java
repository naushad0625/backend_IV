package org.backend_iv.concurrentprogramming._06_web_based_text_editor.save;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/save")
public class DocSaveController {

    @Autowired
    private DocSaveService docSaveService;

    @PostMapping()
    public String save(@RequestBody() SaveRequest request) {
        for (int i = 0; i < 5; i++) {
            docSaveService.submitSaveRequest(request);
        }

        return "Done";
    }

}


