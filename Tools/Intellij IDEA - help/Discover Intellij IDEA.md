Discover Intellij IDEA

### User interface

The following table lists default tool windows and their corresponding shortcuts:

| Tool Window     | Shortcut |
| --------------- | -------- |
| Project         | `⌘1`     |
| Version Control | `⌘9`     |
| Run             | `⌘4`     |
| Debug           | `⌘5`     |
| TODO            | `⌘6`     |

### Editor shortcuts

The following table lists some useful shortcuts related to editing your code:

| Action                                        | Description  |
| --------------------------------------------- | ------------ |
| Move the current line of code                 | `⇧⌘↑`, `⇧⌘↓` |
| Duplicate a line of code                      | `⌘D`         |
| Remove a line of code                         | `⌘⌫`         |
| Comment or uncomment a line of code           | `⌘/`         |
| Comment a block of code                       | `⌥⌘/`        |
| Find in the currently opened file             | `⌘F`         |
| Find and replace in the current file          | `⌘R`         |
| Next occurrence                               | `⌘G`         |
| Previous occurrence                           | `⇧⌘G`        |
| Navigate between opened tabs                  | `⇧⌘]`, `⇧⌘]` |
| Navigate back/forward                         | `⌘[`, `⌘]`   |
| Expand or collapse a code block in the editor | `⌘+`, `⌘-`   |
| Create new...                                 | `⌘N`         |
| Surround with                                 | `⌥⌘T`        |

To expand or shrink the selection based on language constructs, press `⌥↑` and `⌥↓`.

### Navigation

#### Recent files

A real time-saver here is an action called *Recent Files* invoked by pressing `⌘E`.

*Navigate to Class* is available by pressing `⌘O `.

*Navigate to File* works similarly by pressing `⇧⌘O`.

*Navigate to Symbol* is available by pressing `⌥⌘O`.

#### structure

When you are not switching between files, you are most probably navigating within a file. The simplest way to do it is to press `⌘F12`.

Navigation shortcuts include:

| Action                     | Shortcut       |
| -------------------------- | -------------- |
| Search everywhere          | Double `Shift` |
| Navigate to Class          | `⌘O`           |
| Navigate to file           | `⇧⌘O`          |
| Navigate to symbol         | `⌥⌘O`          |
| Recent files               | `⌘E`           |
| File structure             | `⌘F12`         |
| Select in                  | `⌥F1`          |
| Navigate to declaration    | `⌘B`           |
| Navigate to type hierarchy | `⌃H`           |
| Show UML popup             | `⌥⌘U`          |

### Quick pop-ups

| Action              | Shortcut |
| ------------------- | -------- |
| Documentation       | `F1`     |
| Quick definition    | `⌥Space` |
| Show usages         | `⌥⌘F7`   |
| Show implementation | `⌥⌘B`    |

### Refactoring basics

IntelliJ IDEA offers a comprehensive set of automated code refactorings that lead to significant productivity gains when used correctly.

| Action              | Shortcut |
| ------------------- | -------- |
| Rename              | `⇧F6`    |
| Extract variable    | `⌥⌘V`    |
| Extract filed       | `⌥⌘F`    |
| Extract a constant  | `⌥⌘C`    |
| Extract method      | `⌥⌘M`    |
| Extract a parameter | `⌥⌘P`    |
| Inline              | `⌥⌘N`    |
| Copy                | `F5`     |
| Move                | `F6`     |
| Refactor this       | `⌃T`     |

### Finding usages

*Find Usages* helps you quickly find all pieces of code referencing the symbol at the caret (cursor), no matter if the symbol is a class, method, field, parameter, or another statement. Just press ⌥F7 and get a list of references grouped by usage type, module, and file.

### Inspections

*Inspections* are built-in static code analysis tools that help you find probable bugs, locate dead code, detect performance issues, and improve the overall code structure.

Most inspections not only tell you where a problem is, but also provide quick fixes to deal with it right away. Press ⌥⏎ to choose a quick-fix.

### Code style and formatting 

Useful formatting shortcuts:

| Action            | Shortcut |
| ----------------- | -------- |
| Reformat code     | ⌥⌘L      |
| Auto-indent lines | ⌃⌥I      |
| Optimize imports  | ⌃⌥O      |

### Version control basis

Useful VCS shortcuts:

| Action                      | Shortcut |
| --------------------------- | -------- |
| Version Control tool window | ⌘9       |
| VCS operations popup        | ⌃V       |
| Commit changes              | ⌘K       |
| Update project              | ⌘T       |
| Push commits                | ⇧⌘K      |

### Make

By default, IntelliJ IDEA doesn't automatically compile projects on saving. To compile a project, select **Build | Make Project** from the main menu, or press `⌘F9`.

### Running and Debugging

| Action | Shortcut |
| ------ | -------- |
| Run    | `⌃R`     |
| Debug  | `⌃D`     |

| Action              | Shortcut |
| ------------------- | -------- |
| Toggle breakpoint   | `⌘F8`    |
| Step into           | `F7`     |
| Smart step into     | `⇧F7`    |
| Step over           | `F8`     |
| Step out            | `⇧F8`    |
| Resume              | `⌥⌘R`    |
| Evaluate expression | `⌥F8`    |

#### Reloading changes and hot swapping

Sometimes, you need to insert minor changes into your code without shutting down the process. Since the Java VM has a HotSwap feature, IntelliJ IDEA handles these cases automatically when you call **Make**.

### Application Servers

To deploy your application to a server:

1. Configure your artifacts by selecting **File | Project Structure | Artifacts** (done automatically for Maven and Gradle projects).
2. Configure an application server by clicking the **Application Servers** page of the Settings/Preferences dialog.
3. Configure a run configuration by selecting **Run | Edit Configurations**, then specify the artifacts to deploy and the server to deploy them to.

### Working with build tools (Maven/Gradle)

If you want the IDE to synchronize your changes(Maven: pox.xml, Gradle: build.gradle) immediately, do the following:

+ For pom.xml: enable the **Import Maven projects automatically** option in **Maven|Importing**
+ For build.gradle: enable the **Use auto-import** option in **Gradle**