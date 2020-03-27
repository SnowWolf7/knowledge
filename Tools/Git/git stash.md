# **git stash**

## **NAME**

git-stash - Stash the changes in a dirty working directory away

## **SYNOPSIS（概要）**

```java
git stash list [<options>] 

git stash show [<stash>] 

git stash drop [-q|--quiet] [<stash>] 

git stash ( pop | apply ) [--index] [-q|--quiet] [<stash>] 

git stash branch <branchname> [<stash>] 

git stash [push [-p|--patch] [-k|--[no-]keep-index] [-q|--quiet]

[-u|--include-untracked] [-a|--all] [-m|--message

<message>][--] [<pathspec>...]] 

git stash clear

git stash create [<message>]

git stash store [-m|--message <message>] [-q|--quiet] <commit>

```

## **DESCRIPTION**

如果要record当前working directory and the index的状态，但想要返回干净的working directory，请使用`git stash`。该命令保存local modifications并reverts the working directory to match the `HEAD` commit. 

- 列举当前所有stashed modifications：`git stash list`
- 检查stash的所有内容：`git stash show`
- 重新载入：`git stash apply`（可能在另一个commit之上）
- `git stash`不带任何参数，等同于`git stash push`

## **OPTIONS**

### **一**

```java
push [-p|--patch] [-k|--[no-]keep-index] [-q|--quiet] 

[-u|--include-untracked] [-a|--all] [-m|--message <message>] 

[--] [<pathspec>...]] 

```

Save your local modifications to a new stash entry and roll them back to HEAD ( in the working tree and in the index ). The part is optional adn gives the description along with the stashed state.

For quickly making a snapshot, 可以省略 “push”。但是，在此模式下，不允许使用non-option参数来prevent a misspelled subcommand from making an unwanted stash enrty。The two exceptions to this are stash -p which acts as alias for stash push -p and pathspecs, which are allowed after a double hyphen -- for disambiguation. 

如果使用pathspec，那么新的stash entry只会记录匹配pathspec的那些文件的修改状态。The index entries and working tree files 只针对这些文件进行状态回滚。

如果使用--keep-index，all changes already added to the index are left intact.

如果使用--include-untracked，all untracted files are also stashed and then cleaned up with git clean，leaving the working directory in a very clean state. 如果使用--all，所有ignored files are stashed and cleaned in addition to the untracked files. 

使用--patch, you can select hunks from the diff between HEAD and the working tree to be stashed. The stash entry is constructed such that its index state is the same as the index state of your repository, and its worktree contains only the changes you selected interactively. The selected changes are then rolled back from your worktree. 查看 [git-add[1]](https://git-scm.com/docs/git-add) 章节的”Interactive Mode” 了解更多--patch的操作。

The `—patch` option implies `--keep-index`. You can use `--no-keep-index` to override this. 

### **二**

```java
save [-p|--patch] [-k|--[no-]keep-index] [-u|--include-untracked]

[-a|--all] [-q|--quiet] [<message>] 

```

该命令已经被弃用，建议使用git stash push。

### **三**

```java
list[<options>]
```

列举现有的stash entries。

The command takes options applicable to the `git log` command to control what is show and how. 

### **四**

```java
show[<stash>]
```

展示stash之前和之后的区别，如果没有给出，展示最后一个。By default, the command shows the diffstat, but it will accept any format known to `git diff`(e.g., `git stash show -p stash@{1}` to view the second most recent entry in patch form). You can use stash.showStat and/or stash.showPatch config variables to change the default behavior. 

### **五**

```java
pop--index[<stash>]
```

Remove a single stashed state from the state list and apply it on top of the current working tree state, i.e., do the inverse operation of `git stash push`. The working directory must match the index. 

Applying the state can fail with conflicts; in this case, it is not removed from the stash list. You need to resolve the conflicts by hand and call `git stash drop` manually afterwards. 

If the `--index`is option is used, then tries to reinstate not only the working tree’s changes, but also the index’s ones. However, this can fail, when you have conflicts (which are stored in the index, where you therefore can no longer apply the changes as they were originally). 

When no `<stash>` is given, `stash@{0}` is assumed, otherwise `<stash>` must be a reference of the form `<stash@{revision>}`. 

### **六**

```java
apply--index[<stash>]
```

Like `pop`, but do not remove the state from the stash list. Unlike `pop`, `<stash>`may be any commit that looks like a commit created by `stash push` or `stash create`. 

### **七**

```java
branch<branchname>[<stash>]
```

Creates and checks out a new branch named `<branchname>` starting from the commit at which the `<stash>` was originally created, applies the changes recorded in `<stash>` to the new working tree and index. If that succeeds, and `<stash>` is a reference of the form `stash@{<version>}`, it then drops the `<stash>`. When no `<stash>` is given, applies the lasest one. 

This is useful if the branch on which you ran `git stash push` has changed enough that `git stash` apply fails due to the conflicts. Since the stash entry is applied on top of the commit that was `HEAD` at the time `git stash` was run, it restores the originally stashed state with no conflicts. 

### **八**

```java
clear
```

Remove all the stash entries. *注意：该操作可能无法回退* 

### **九**

```java
drop[-q|--quiet][<stash]
```

Remove a single stash entry from the list of stash entries. When no `<stash>` is given, it removes the latest one.i.e. `stash@{0}`, otherwise`<stash>` must be a valid stash log reference of the form `stash@{<revision>}`. 

### **十**

```java
create
```

Create a stash entry (which is a regular commit object) and return its object name, without storing it anywhere in the ref namespace. 这对脚本非常有用，如果用命令行，请参考git stash push。

### **十一**

store

Store a given stash created via *git stash create* (which is a dangling merge commit) in the stash ref, updating the stash reflog. 这对脚本非常有用，如果用命令行，请参考`git stash push`。 

## **DISCUSSION**

A stash entry is represented as a commit whose tree records the state of the working directory, and its first parent is the commit at `HEAD` when the entry was created. The tree of the second parent records the state of the index when the entry is made, and it is made a child of the `HEAD` commit. The ancestry graph looks like this: 

```java
					.----W
				 /		/ 
		----H----I

```

where `H` is the `HEAD` commit, `I` is a commit that records the state of the index, and `W` is a commit that records the state of the working tree. 

## **EXAMPLES**

### **Pulling into a dirty tree**

```java
git stash

git pull

git stash pop 
```

### **Interrupted workflow**

```java
git stash

edit emergency fix 

git commit -a -m "Fix in a hurry" 

git stash pop 

```

### **Testing partial commits**

You can use `git stash push --keep-index` when you want to make two or more commits out of the changes in the work tree, any you want to test each change before committing: 

```javascript
git add --patch foo 					# add just first part to the index 

git stash push --keep-index 	# save all other changes to the stash 

edit/build/test first part 

git commit -m 'First part' 		# commit fully tested change 

git stash pop 								# prepare to work on all other changes 

... repeat above five steps until one commit remains... 

edit/build/test remaining parts

git commit foo -m 'Remaining parts' 

```

### **Recovering stash entries that were cleared/dropped erroneously**

If you mistakenly drop or clear stash entries, they cannot be recovered through the normal safety mechanisms. However, you can try the following incantation to get a list of stash entries that are still in your repository, but not reachable any more：

```java
git fsck --unreachable | 

grep commit | cut -d\ -f3 | 

xargs git log --merges --no-walk --grep=WIP 
```

