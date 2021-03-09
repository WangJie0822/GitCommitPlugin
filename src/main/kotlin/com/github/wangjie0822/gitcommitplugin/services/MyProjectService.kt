package com.github.wangjie0822.gitcommitplugin.services

import com.github.wangjie0822.gitcommitplugin.MyBundle
import com.intellij.openapi.project.Project

class MyProjectService(project: Project) {

    init {
        println(MyBundle.message("projectService", project.name))
    }
}
