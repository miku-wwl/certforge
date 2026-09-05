# Pushpay Senior SRE — 140 个主题 × 5 道简答题（中文版 Study Edition）

> **共 700 道简答题，每题包含问题与参考答案。**

每个 Topic 严格按照以下五层展开：

1. **`.1` — 基础定义**
2. **`.2` — 内部机制**
3. **`.3` — 生产场景**
4. **`.4` — 故障排查**
5. **`.5` — Senior Trade-off / Edge Case**

参考答案用于建立正确 mental model 和面试表达，不建议逐字背诵。真实面试中，应先直接回答问题，再补充 reasoning、evidence、production example 和 trade-off。

---

# 01 — Git 内部原理

## Q1 — .git 目录

### Q1.1 — 基础定义

**问题：** 描述 `.git` 目录中包含什么，以及主要文件和子目录分别有什么作用。

**参考答案：**

`.git` 保存仓库元数据。核心内容包括：`HEAD`（当前引用）、`config`、`index`（暂存区）、`objects/`（内容寻址对象库）、`refs/`（分支和标签引用）、`logs/`（reflog）、`hooks/`，以及有时出现的 `packed-refs`。

### Q1.2 — 内部机制

**问题：** 解释 `HEAD`、`refs`、`index`、`objects`、`logs` 和 `packed-refs` 之间的关系。

**参考答案：**

`HEAD` 通常符号指向某个 branch ref；branch ref 指向 commit。commit 再指向 tree，tree 指向 blob 或子 tree。`index` 表示工作区和 `HEAD` 之间准备提交的快照，reflog 记录引用移动历史。

### Q1.3 — 生产场景

**问题：** 开发者说工作区内容正确，但 Git 显示了意外的 staged changes。你会检查 `.git` 中哪些结构，为什么？

**参考答案：**

先用 `git status`、`git diff` 和 `git diff --cached` 区分 working tree、index 与 `HEAD` 的差异。如果工作区正确而 staged state 错误，重点检查 index，不要在没确认影响前直接删除 `.git/index`。

### Q1.4 — 故障排查

**问题：** 一次错误操作后，分支指针异常，而且几个 commit 看起来丢失了。请基于 Git internals 说明恢复思路。

**参考答案：**

先保护当前仓库副本，避免继续 GC 或写入。查看 `git reflog`、branch refs、`HEAD`、`git fsck --lost-found`，并与 remote 或其他 clone 对比。确认正确 commit graph 后先建立临时 recovery ref，再修复正式分支。

### Q1.5 — Senior Trade-off / Edge Case

**问题：** 如果 reflog 不完整，而且可能已经执行过 garbage collection，还有哪些恢复手段？首先要保护什么证据？恢复能力的极限在哪里？

**参考答案：**

如果 reflog 已过期且 unreachable objects 被 GC 真正删除，恢复能力会显著下降。第一步应保存所有 clone、object 目录和 CI workspace，并搜索 dangling objects、其他开发者副本和备份。一旦对象在所有副本中都被物理删除，Git 本身无法重建。

---

## Q2 — Git commit / tree / blob 对象

### Q2.1 — 基础定义

**问题：** Git commit object 到底是什么？

**参考答案：**

commit object 保存父 commit、author/committer、message，以及根 tree 的 hash。它本身不是一份可变的完整源码副本，而是通过 tree/blob 对象引用一个不可变快照。

### Q2.2 — 内部机制

**问题：** 解释 commit、tree、blob 和 annotated tag 对象如何连接，以及 Git 如何进行 content addressing。

**参考答案：**

blob 保存文件内容；tree 保存文件名到 blob/子 tree 的映射；commit 指向根 tree 和父 commit；annotated tag 可以指向其他对象。对象 ID 本质上由对象类型、长度和内容计算出的 hash 标识。

### Q2.3 — 生产场景

**问题：** 两个仓库包含相同文件内容，但提交历史不同。哪些对象可能相同，哪些通常会不同？

**参考答案：**

相同文件内容可以生成相同 blob；如果目录结构和内容完全一致，相应 tree 也可能相同。但 commit 因 parent、时间、作者、message 或根 tree 不同而通常不同。

### Q2.4 — 故障排查

**问题：** 一个仓库看起来损坏了。你如何利用 object structure 和 `git fsck` 输出定位损坏范围？

**参考答案：**

运行 `git fsck` 找到缺失或损坏的对象类型和 hash。若某个 blob 丢失，commit/tree 可能仍在，但工作树无法完整恢复。优先从 healthy remote、其他 clone、backup 或 packfile 恢复对象。

### Q2.5 — Senior Trade-off / Edge Case

**问题：** Git 的 immutable content-addressed storage 带来了哪些优势？hash collision、packfile 和 GC 又会带来什么复杂度？

**参考答案：**

不可变内容寻址让去重、完整性验证和廉价分支成为可能。packfile 提高压缩效率但增加人工分析复杂度；GC 会清理不可达对象；现代 Git 对 hash collision 有额外防护，但仍需要理解对象生命周期。

---

## Q3 — `git add` 与 index

### Q3.1 — 基础定义

**问题：** 执行 `git add` 时内部发生了什么？

**参考答案：**

`git add` 会把希望进入下一次 commit 的内容写入 index，并按需把文件内容写成 blob object。index 记录 path、mode、blob ID 等 staged snapshot 信息。

### Q3.2 — 内部机制

**问题：** Git index 到底是什么？它与 working tree 和 `HEAD` 有什么区别？

**参考答案：**

working tree 是磁盘上当前文件；index 是下一次 commit 的暂存快照；`HEAD` 是当前已提交快照。commit 是从 index 创建的，而不是直接从任意 working-tree 内容创建。

### Q3.3 — 生产场景

**问题：** 一个文件只 stage 了部分修改。Git 如何表示这种状态？

**参考答案：**

index 可以保存只包含已选择 hunks 的 blob，而 working-tree 文件还能保留额外 unstaged changes。因此同一个 path 可以同时在 `HEAD`、index 和 working tree 中有三种版本。

### Q3.4 — 故障排查

**问题：** 事故修复中，开发者错误 stage 了不该提交的改动。如何在不丢失工作区内容的情况下安全修正？

**参考答案：**

先用 `git diff --cached` 看 staged 内容，用 `git diff` 看 unstaged 内容。可使用 `git restore --staged`、`git reset <path>` 或 interactive staging 修正 index，同时保留 working tree，提交前再次确认。

### Q3.5 — Senior Trade-off / Edge Case

**问题：** 为什么 index 是 Git 很强的设计？脚本并发或错误操作 index 会有什么风险？

**参考答案：**

index 让精确提交、部分暂存和 merge state 成为可能，但错误脚本可能 stage 意外数据或与其他 Git 写操作竞争。CI 应使用隔离 workspace，并避免多个 writer 同时操作同一个 checkout。

---

## Q4 — Branch 与 refs

### Q4.1 — 基础定义

**问题：** Git branch 本质上是什么？

**参考答案：**

branch 本质上是一个有名字的 ref，指向某个 commit。它不是一个物理保存 commits 的容器。

### Q4.2 — 内部机制

**问题：** commit、merge、reset 和 fast-forward 时 branch ref 如何移动？

**参考答案：**

普通 commit 会让当前 branch ref 前移；fast-forward 直接把 ref 移到后代 commit；merge 可能创建多 parent commit；reset 直接移动 ref；rebase 创建新 commit 后再移动 branch。

### Q4.3 — 生产场景

**问题：** 有人说“branch 里面包含一组 commits”。如何用 reachability 更精确地解释？

**参考答案：**

更准确的说法是：如果某 commit 从 branch tip 沿 parent graph 可达，就可以认为它“在这个 branch 的历史上”。同一个 commit 可以被多个 branch 同时 reach。

### Q4.4 — 故障排查

**问题：** release branch 被 force-push 到错误 commit。如何找到旧 tip 并安全恢复？

**参考答案：**

先检查 local/remote reflog、服务器审计、其他 clone 和 `git fsck`，找到旧 tip 后先建立临时 recovery branch，验证历史，再协调团队使用 `--force-with-lease` 或受控方式恢复共享分支。

### Q4.5 — Senior Trade-off / Edge Case

**问题：** 什么时候 force-push 可以接受？生产工程流程中如何降低 ref 丢失风险？

**参考答案：**

force-push 更适合个人或明确允许改写历史的分支。共享/release branch 应设置 protection、review、审计和备份；优先 `--force-with-lease`，避免无条件覆盖他人的新提交。

---

## Q5 — HEAD 与 detached HEAD

### Q5.1 — 基础定义

**问题：** `HEAD` 在 Git 中是什么？

**参考答案：**

`HEAD` 表示当前 checkout 的 commit，通常通过符号引用间接指向类似 `refs/heads/main` 的 branch ref。

### Q5.2 — 内部机制

**问题：** 解释 symbolic `HEAD` 与 detached `HEAD` 的区别，以及两种状态下 commit 会发生什么。

**参考答案：**

正常 branch 状态下，新 commit 会推进 branch ref；detached HEAD 时，新 commit 存在于 object database，但不会自动由 branch 指向，后续切换可能让它变成 unreachable。

### Q5.3 — 生产场景

**问题：** CI 直接 checkout 某个 commit SHA，然后在构建中创建了新 commit。这个 commit 会在哪里？

**参考答案：**

CI 通常 checkout 精确 SHA 来保证可复现，因此处于 detached HEAD。新 commit 会存在于该 workspace 的对象库中，但除非创建 ref 或 push，否则不会被长期保留。

### Q5.4 — 故障排查

**问题：** 有人在 detached HEAD 中做了重要修改并 commit，随后切换了 branch。如何恢复？

**参考答案：**

使用 `git reflog` 找到该 commit SHA，然后执行如 `git branch recovery <sha>` 创建引用。先确认内容，再继续其他操作。

### Q5.5 — Senior Trade-off / Edge Case

**问题：** 为什么 CI/CD 经常故意使用 detached HEAD？这对人工操作有什么风险？

**参考答案：**

CI 使用 detached HEAD 能保证针对不可变 commit 构建，但人工环境中容易产生未被 branch 引用的工作，因此需要清楚 reflog 和 recovery 方法。

---

## Q6 — Merge 与 Rebase

### Q6.1 — 基础定义

**问题：** merge 和 rebase 的区别是什么？

**参考答案：**

merge 保留原历史并通过 fast-forward 或新 merge commit 连接分支；rebase 会把一系列逻辑修改重新应用到新 base，从而创建新的 commit IDs。

### Q6.2 — 内部机制

**问题：** 从 Git object graph 的角度解释 merge 与 rebase 分别改变了什么。

**参考答案：**

merge 保留原 parent graph；rebase 会重新创建 commit，并让这些新 commit 指向新的 parent，因此 hash 发生变化，即使最终文件内容相似。

### Q6.3 — 生产场景

**问题：** 一个长期 feature branch 与 main 严重分叉。如何选择 merge、rebase 或重新整理分支？

**参考答案：**

私有分支可用 rebase 简化历史；已共享或需要审计上下文时 merge 更安全。若历史非常混乱，建立新 branch 并选择性 cherry-pick 有时风险更低。

### Q6.4 — 故障排查

**问题：** 共享 branch 被 rebase 后，协作者看到重复样的 commits。如何处理？

**参考答案：**

先明确 canonical history，然后让协作者 reset/rebase 到正确分支，避免再次 merge 两套逻辑相同但 hash 不同的提交。需要团队协调，不能各自随意修复。

### Q6.5 — Senior Trade-off / Edge Case

**问题：** linear history 与保留 merge history 在可维护性、审计和事故取证上分别有什么 trade-off？

**参考答案：**

linear history 容易阅读和 bisect；merge history 保留集成上下文和真实历史。强调 incident forensic 的环境中，有意义的 merge boundary 往往比“历史漂亮”更重要。

---

## Q7 — Git 仓库损坏

### Q7.1 — 基础定义

**问题：** Git repository corruption 是什么意思？

**参考答案：**

仓库损坏指 Git 元数据或对象数据不再满足预期完整性，例如 refs 指错、index 损坏、object 丢失或 packfile 损坏。

### Q7.2 — 内部机制

**问题：** refs 损坏与 objects 损坏有什么区别？

**参考答案：**

ref corruption 可能只让 branch 指向错误/不存在的 commit，而 object 仍完整；object corruption 则意味着真正的内容或历史对象丢失/损坏，通常更严重。

### Q7.3 — 生产场景

**问题：** 只有一个 build agent 报 Git object error，而其他 clone 都正常。如何区分本地磁盘问题与 remote repository 问题？

**参考答案：**

如果只有单个 agent 失败，优先怀疑本地磁盘、filesystem、cache 或 workspace。运行 `git fsck`，与 fresh clone 对比 object IDs，并查看存储错误。

### Q7.4 — 故障排查

**问题：** 请给出一个安全的 corruption investigation 流程。

**参考答案：**

先做只读副本，停止 GC/自动清理；检查 `git fsck`、reflog、refs、remote 和 backup。确定对象完整性后再修 refs，缺失对象优先从健康 clone/remote 恢复。

### Q7.5 — Senior Trade-off / Edge Case

**问题：** 在受监管生产环境中，修复前应如何做 evidence preservation 和 recovery？

**参考答案：**

先做 forensic copy，记录 hash、命令和时间线；限制 destructive repair；优先从 immutable known-good backup 恢复。目标不仅是“能用”，还要保留审计链。

---

## Q8 — 丢失 commit 的恢复

### Q8.1 — 基础定义

**问题：** 误执行 reset 后如何恢复 commits？

**参考答案：**

最常见做法是使用 `git reflog` 找到 reset 前的 commit SHA，然后立即创建 recovery branch/tag 指向它。

### Q8.2 — 内部机制

**问题：** 解释 reflog、unreachable objects 和 reachability 在恢复中的关系。

**参考答案：**

reflog 记录 ref 和 `HEAD` 的移动。commit 即使不再被任何 branch reach，只要 object 仍存在就可能恢复；GC 可能在到期后清理 unreachable objects。

### Q8.3 — 生产场景

**问题：** 开发者 `reset --hard` 后又 force-push，remote 已不再引用三个 commits。怎么办？

**参考答案：**

检查执行 force-push 的本地 reflog、其他开发者 clone、CI workspace 和服务器端引用历史。找到 commit 后先建立临时 ref，验证后再恢复共享 branch。

### Q8.4 — 故障排查

**问题：** remote 没 reflog，而且 branch/ref 都找不到这些 commits，如何继续搜索？

**参考答案：**

可以使用 `git fsck --dangling/--lost-found` 搜索 dangling commits，并检查其他 clone、构建缓存、patch、备份。只要 raw object 仍在，就可能重建一部分 graph。

### Q8.5 — Senior Trade-off / Edge Case

**问题：** GC 如何影响恢复窗口？哪些 repository policy 可以降低永久丢失概率？

**参考答案：**

GC 会缩短 unreferenced object 的可恢复窗口。应保护重要 branch、保留 reflog/backup、使用 `--force-with-lease`，并让 release refs 尽量不可变。

---

# 02 — Linux / 操作系统

## Q9 — Process 与 Thread

### Q9.1 — 基础定义

**问题：** 解释 process 和 thread 的区别。

**参考答案：**

process 是相对隔离的执行环境，通常拥有独立虚拟地址空间和资源；thread 是同一 process 内的执行流，共享大量进程状态。

### Q9.2 — 内部机制

**问题：** threads 之间共享哪些资源，哪些状态是每个 thread 私有的？

**参考答案：**

threads 通常共享 code、heap、open file descriptors 等；每个 thread 有自己的 registers、stack、scheduling state 和 thread-local storage。

### Q9.3 — 生产场景

**问题：** 一个多线程服务 CPU 很高，但只有一个 core 被打满。可能是什么原因？

**参考答案：**

可能存在单线程热点、串行关键区、锁竞争、CPU affinity、GC/runtime 行为，或者算法本身无法并行。多线程并不保证负载自动均匀分布到所有 core。

### Q9.4 — 故障排查

**问题：** 如何判断瓶颈来自 lock contention、scheduler 还是某个 hot thread？

**参考答案：**

使用 `top -H`、`pidstat -t`、runtime thread dump、`perf` 或 profiler 看 per-thread CPU 和 blocked/wait 状态，并与锁和调用栈关联。

### Q9.5 — Senior Trade-off / Edge Case

**问题：** 从可靠性、隔离和运维角度，什么时候更适合多进程而不是多线程？

**参考答案：**

多进程隔离更强、故障边界和资源控制更清晰；多线程通信成本低、共享内存方便。选择应基于语言 runtime、并行模型、隔离需求和 blast radius。

---

## Q10 — Linux 进程内存布局

### Q10.1 — 基础定义

**问题：** 描述典型 Linux process 的内存布局。

**参考答案：**

典型布局包括 executable text、initialized data、BSS、heap、每线程 stack、shared libraries 和 memory-mapped regions。具体地址受 loader、runtime 和 ASLR 影响。

### Q10.2 — 内部机制

**问题：** 解释 text、data、BSS、heap、stack、shared libraries 和 mmap regions。

**参考答案：**

text 是代码；data/BSS 保存全局数据；heap 用于动态分配；stack 保存调用帧；mmap 可用于文件映射、共享库、匿名内存和共享内存。

### Q10.3 — 生产场景

**问题：** process RSS 很高，但 heap 使用不高。可能是什么原因？

**参考答案：**

高 RSS 可能来自 native allocations、thread stacks、mmap、shared libraries、allocator fragmentation、direct buffers 或 resident file-backed pages，而不仅是语言 heap。

### Q10.4 — 故障排查

**问题：** 如何调查 anonymous mappings、shared mappings、page cache effects 和 native allocations？

**参考答案：**

可查看 `/proc/<pid>/smaps`、`pmap`、runtime native-memory 工具、cgroup counters 和 allocation profile，区分 anonymous/private、file-backed 和 shared memory。

### Q10.5 — Senior Trade-off / Edge Case

**问题：** 容器中哪些 memory metrics 最容易误导？如何同时看 host 和 cgroup 的真实 memory pressure？

**参考答案：**

RSS、working set、cache、runtime heap 和 cgroup usage 表示不同概念。容器中应同时比较进程内存、cgroup limit/current 和 host pressure，不能只看 JVM/Go heap。

---

## Q11 — Linux Load Average

### Q11.1 — 基础定义

**问题：** Linux load average 到底测量什么？

**参考答案：**

load average 表示一段时间内 runnable tasks 与 uninterruptible sleep tasks 的平均数量，通常给出约 1、5、15 分钟三个值。它不等同于 CPU utilization。

### Q11.2 — 内部机制

**问题：** 为什么 CPU utilization 很低时 load average 仍然可能很高？

**参考答案：**

大量任务处于 `D` state 等待 I/O 时也会计入 load，因此 CPU 可能空闲但 load 很高；短时 runnable burst 也可能产生类似现象。

### Q11.3 — 生产场景

**问题：** 16 核主机 load=30，但 CPU 只有 25%。你的主要 hypotheses 是什么？

**参考答案：**

优先怀疑 blocked I/O、storage/network filesystem latency，或大量 runnable burst。先看 task state，再决定是不是 CPU saturation。

### Q11.4 — 故障排查

**问题：** 如何证明 blocked I/O 是否导致高 load？

**参考答案：**

查看 `ps`/`top` 中 `R` 和 `D` state，结合 `vmstat`、`iostat`、`pidstat`、storage latency/queue 和 kernel log，对齐时间线验证。

### Q11.5 — Senior Trade-off / Edge Case

**问题：** 如何设计 load average 告警，避免大量噪声？

**参考答案：**

不要只用 raw load 静态阈值 page。应结合 load-per-core、CPU saturation、D-state 数量、I/O latency 和用户影响；load 更适合作为诊断/预测信号。

---

## Q12 — CPU Saturation

### Q12.1 — 基础定义

**问题：** 生产环境 CPU 突然达到 100%，第一步怎么做？

**参考答案：**

先确认用户影响，并判断 CPU 是 host-wide、container-specific 还是某个 process/thread 特定，再对齐流量与最近 change。

### Q12.2 — 内部机制

**问题：** 解释 user、system、iowait、steal、IRQ、softirq 分别如何影响判断。

**参考答案：**

user time 高多为应用计算；system time 高偏 kernel；iowait 表示 CPU 空闲等待 I/O；steal 可能是虚拟化争用；IRQ/softirq 高可能与网络/设备处理有关。

### Q12.3 — 生产场景

**问题：** Kubernetes node 在 deployment 后 CPU 饱和。如何区分应用 CPU、kernel overhead 和 noisy neighbour？

**参考答案：**

比较 Pod/container CPU、cgroup throttling、node CPU modes、per-process/thread usage 和 deployment version。应用平均指标正常也可能存在单 core 热点。

### Q12.4 — 故障排查

**问题：** 哪些工具可以定位 hot process、thread、syscall 和 code path，同时尽量不放大事故？

**参考答案：**

可使用 `top`、`pidstat`、`mpstat`、`perf`、runtime profiler、thread dump 和 syscall/network metrics。事故中优先低开销 sampling，慎用重型 tracing。

### Q12.5 — Senior Trade-off / Edge Case

**问题：** 什么时候应该 throttling、scaling、rollback 或 optimization？如何选择？

**参考答案：**

明确新 release 导致用户问题时 rollback；合法负载且可横向扩展时 scaling；依赖承压时 load shedding/throttling；optimization 应基于 profiling，而不是凭感觉改代码。

---

## Q13 — Memory Leak / Memory Growth

### Q13.1 — 基础定义

**问题：** 如何区分真正的 memory leak 与正常 memory growth？

**参考答案：**

leak 指不再有业务价值的内存持续被保留且不能回到稳定基线；正常增长可能来自 cache、warm-up、数据规模或 allocator 行为。

### Q13.2 — 内部机制

**问题：** 解释 heap、native memory、page cache、mmap、allocator fragmentation 和 cgroup accounting。

**参考答案：**

total memory 不只有 managed heap，还包括 native allocations、stacks、mmap、direct buffers、fragmentation 和 page cache；cgroup 统计可能包含 runtime 看不到的部分。

### Q13.3 — 生产场景

**问题：** Java container 内存不断接近 limit，但 JVM heap 只有 60%。主要 hypotheses 是什么？

**参考答案：**

检查 native memory、direct buffers、thread count/stacks、metaspace、code cache、mmap、JNI/native libs 和 file cache。

### Q13.4 — 故障排查

**问题：** 如何结合 container metrics、JVM/native data、`/proc` 和系统工具定位？

**参考答案：**

比较 cgroup memory、JVM NMT、`/proc/<pid>/smaps`、thread count、direct-buffer metrics、open files 和 workload changes，观察是哪一类内存随时间增长。

### Q13.5 — Senior Trade-off / Edge Case

**问题：** 事故中最安全的 remediation 是什么？长期如何防止复发？

**参考答案：**

高 OOM 风险时可减载或安全重启作为 mitigation，同时尽量保留证据。长期应设置 memory budget/headroom、限制 native/direct growth，并进行真实并发 load test。

---

## Q14 — Zombie Process

### Q14.1 — 基础定义

**问题：** 什么是 zombie process？

**参考答案：**

zombie 是已经退出、但 parent 尚未调用 `wait()` 收集 exit status 的进程。它不执行代码，只保留 PID table entry。

### Q14.2 — 内部机制

**问题：** 为什么 zombie 不能直接被 kill？

**参考答案：**

它已经“死了”，所以再发送 kill 没意义。需要 parent reap；或者 parent 退出，让 init/reaper 接管并回收。

### Q14.3 — 生产场景

**问题：** 一个容器积累了几千个 zombies，这通常说明 PID 1 或 child reaping 有什么问题？

**参考答案：**

通常说明 PID 1 或其他 parent 没有正确 reap children，尤其是直接用普通应用进程作为容器 PID 1 时容易出现。

### Q14.4 — 故障排查

**问题：** 如何找到 parent process、确认机制并安全缓解？

**参考答案：**

使用 `ps` 查看 `Z` state 和 PPID，定位 parent，并分析 child spawning 逻辑。修复或重启 parent；必要时使用 `tini` 等最小 init。

### Q14.5 — Senior Trade-off / Edge Case

**问题：** 容器 entrypoint/init 设计如何避免 zombie accumulation？

**参考答案：**

entrypoint 应正确转发 signals 并 reap children，或者使用专门 init。不要产生无人管理的 subprocess，并监控 PID pressure。

---

## Q15 — File Descriptors

### Q15.1 — 基础定义

**问题：** 什么是 file descriptor？

**参考答案：**

file descriptor 是进程内的整数句柄，指向 kernel 中打开的对象，例如普通文件、socket、pipe、eventfd 或 device。

### Q15.2 — 内部机制

**问题：** 文件、socket、pipe 等如何通过 file descriptor 表示？

**参考答案：**

进程 descriptor table 把整数映射到 open-file descriptions/kernel objects，因此 network socket 也会消耗同一类 descriptor limit。

### Q15.3 — 生产场景

**问题：** 应用开始报 `Too many open files`，最可能发生了什么？

**参考答案：**

通常是 process/system descriptor limit 达到上限，原因可能是 socket/file leak、并发量合法增长、连接关闭变慢或异常资源生命周期。

### Q15.4 — 故障排查

**问题：** 如何利用 `/proc`、`lsof`、`ss`、limits 和应用 metrics 判断是 leak 还是合法增长？

**参考答案：**

查看 `/proc/<pid>/fd`、`lsof`、`ss`、进程 limits，以及 descriptor count 随时间变化，并按类型/目标分组，判断 socket leak、file leak 还是正常 concurrency。

### Q15.5 — Senior Trade-off / Edge Case

**问题：** 什么时候提高 `ulimit` 是正确的？什么时候只是在掩盖设计问题？

**参考答案：**

若业务确实需要更高并发且下游/内核能承受，提高 limit 合理；若 fd 数无限增长，提高 `ulimit` 只是延迟故障并扩大 blast radius。

---

## Q16 — SIGTERM 与 SIGKILL

### Q16.1 — 基础定义

**问题：** SIGTERM 与 SIGKILL 有什么区别？

**参考答案：**

SIGTERM 是可捕获的优雅终止请求；SIGKILL 不能被捕获、阻塞或处理，由 kernel 直接终止进程。

### Q16.2 — 内部机制

**问题：** kernel 与 application 对这两个 signal 分别能做什么？

**参考答案：**

收到 SIGTERM 时应用可以停止接流量、完成/放弃 in-flight work、flush state、关闭连接；SIGKILL 跳过这些 cleanup。

### Q16.3 — 生产场景

**问题：** 一个 Kubernetes Pod 总要靠 `kill -9` 才能结束，可能有什么问题？

**参考答案：**

应用可能忽略 SIGTERM、PID 1 没转发 signal、shutdown deadlock、blocked I/O，或者 preStop/cleanup 超过 grace period。

### Q16.4 — 故障排查

**问题：** 如何调查 stuck shutdown、blocked I/O、deadlock、preStop hook 和 termination grace period？

**参考答案：**

检查 shutdown logs、thread dumps、preStop、signal delivery、open connections 和 blocked syscalls，确认应用是否收到 SIGTERM，以及卡在哪一步。

### Q16.5 — Senior Trade-off / Edge Case

**问题：** 一个生产服务应该实现怎样的 graceful shutdown contract？

**参考答案：**

生产服务应停止接新流量，安全处理 in-flight requests，释放 lease/connection，并在有界时间内退出。grace period 要同时兼顾 correctness 与恢复速度。

---

# 03 — 网络

## Q17 — 打开 HTTPS URL 时发生什么

### Q17.1 — 基础定义

**问题：** 在浏览器输入 `https://example.com` 后发生什么？

**参考答案：**

通常会先 DNS 解析，再选择路由并解析 next-hop 邻居，然后建立 TCP，进行 TLS handshake，发送 HTTP 请求，随后经过 LB/proxy 到应用和下游依赖。

### Q17.2 — 内部机制

**问题：** 按顺序说明 DNS、route selection、neighbour resolution、TCP、TLS、HTTP、proxy/load balancer 和 application handling。

**参考答案：**

DNS 把 name 映射成 address；L2/L3 负责到达；TCP 建连接；TLS 做身份认证和加密；HTTP 承载请求；中间代理可能 terminate 或转发连接。

### Q17.3 — 生产场景

**问题：** DNS 已解析成功，但页面一直打不开。还有哪些层可能有问题？

**参考答案：**

routing、firewall、TCP handshake、TLS、proxy/LB、application listener、downstream dependency 都仍可能失败。

### Q17.4 — 故障排查

**问题：** 如何利用 packet capture、socket state、TLS 工具和 telemetry 缩小故障范围？

**参考答案：**

逐层验证：`dig`、route、`tcpdump`、`ss`、`curl -v`、`openssl s_client`、LB logs、backend telemetry、trace。找到 healthy 与 failing path 第一个分叉点。

### Q17.5 — Senior Trade-off / Edge Case

**问题：** 为了让 timeout 容易诊断，你会在整条链路上如何布置 observability 和 timeout？

**参考答案：**

为 DNS/connect/TLS/request 分别记录耗时，配置清晰的 per-hop timeout，传播 trace/context，并保留 edge/LB/proxy access logs 和 end-to-end synthetic checks。

---

## Q18 — TCP 三次握手

### Q18.1 — 基础定义

**问题：** 解释 TCP three-way handshake。

**参考答案：**

TCP 使用 SYN → SYN-ACK → ACK 来建立双向状态，并同步双方初始 sequence numbers。

### Q18.2 — 内部机制

**问题：** 为什么需要交换 sequence numbers？为什么不是 two-way handshake？

**参考答案：**

双方都需要确认对方能收发，并确认 sequence state。三次消息让双向 reachability 和 state synchronization 都得到确认。

### Q18.3 — 生产场景

**问题：** 只有 connection establishment 间歇失败，但已建立连接都正常，这暗示什么？

**参考答案：**

更可能是 SYN/SYN-ACK 丢失、listen backlog、NAT/firewall、connection-rate limit 等建立阶段问题，而不是请求处理逻辑。

### Q18.4 — 故障排查

**问题：** 如何区分 SYN drop、listen backlog exhaustion、firewall 问题和 server overload？

**参考答案：**

检查 SYN retransmits、listen backlog、`ss -s`、SYN cookies、firewall counters、LB metrics、packet captures 和 accept rate，并对比成功/失败路径。

### Q18.5 — Senior Trade-off / Edge Case

**问题：** 面对真实连接突发与攻击/retry storm，分别应该如何调整？

**参考答案：**

合法 burst 可通过 connection reuse、扩大 listener capacity 和适当 backlog；攻击或 retry amplification 则需要 edge rate limit 和保护，而不是无脑加 kernel limits。

---

## Q19 — TIME_WAIT

### Q19.1 — 基础定义

**问题：** `TIME_WAIT` 是什么，为什么存在？

**参考答案：**

`TIME_WAIT` 是 TCP 连接关闭后保留的一段状态，用来防止旧延迟 segment 干扰新连接，并允许最后 ACK 的重传。

### Q19.2 — 内部机制

**问题：** 为什么 active closer 常进入 `TIME_WAIT`？

**参考答案：**

active closer 通常保留 tuple 一段时间，避免相同 endpoint/port 组合太快复用导致 sequence-space 混乱。

### Q19.3 — 生产场景

**问题：** 大量 TIME_WAIT 导致 outbound connections 失败，可能发生了什么？

**参考答案：**

短连接太多且没有 keepalive/pooling 时，客户端可能耗尽 ephemeral ports，或者让 NAT/LB connection tracking 资源达到上限。

### Q19.4 — 故障排查

**问题：** 如何调查 ephemeral-port exhaustion、connection reuse、NAT limits 和 client behaviour？

**参考答案：**

查看 socket states、source port 使用率、keepalive/pool、NAT table/limits、目标地址分布，判断瓶颈在 host port 还是网络设备。

### Q19.5 — Senior Trade-off / Edge Case

**问题：** 哪些 mitigation 是安全的？为什么激进 TIME_WAIT tuning 可能有风险？

**参考答案：**

优先使用 connection pooling/keepalive 和合理客户端设计。内核级 aggressive reuse 可能与旧包、NAT 或中间设备交互出错，应充分验证后再调。

---

## Q20 — DNS 成功但 TCP Timeout

### Q20.1 — 基础定义

**问题：** hostname 能解析，但连接 timeout。下一步检查什么？

**参考答案：**

检查 route、security/firewall、TCP handshake、NAT/proxy path、target listener/health 和 application availability。

### Q20.2 — 内部机制

**问题：** 解释 DNS 成功到应用连接成功之间还经过哪些层。

**参考答案：**

DNS 只提供 IP，后续仍需要可达路由、允许的网络 policy、TCP listener、可能的 NAT/proxy/TLS，最后才到应用。

### Q20.3 — 生产场景

**问题：** 只有一个 subnet 出现 timeout，而其他 subnet 正常。这如何改变 hypotheses？

**参考答案：**

优先比较该 subnet 的 route table、NACL、NAT/egress path、network appliance 和 subnet association，因为 SG 相同而 subnet 不同。

### Q20.4 — 故障排查

**问题：** 如何检查 route table、SG/NACL、NAT、proxy、target health 和 packet evidence？

**参考答案：**

结合 Reachability Analyzer（适用时）、VPC Flow Logs、route/NACL/SG review、`tcpdump`、SYN retransmits、`nc/curl` 和 server-side capture。

### Q20.5 — Senior Trade-off / Edge Case

**问题：** 如何设计诊断数据，让团队快速区分 DNS、routing、transport、TLS 和 application failure？

**参考答案：**

对 DNS lookup、TCP connect、TLS handshake、TTFB/HTTP request 分阶段打点和分类错误，而不是只有一个 generic timeout。

---

## Q21 — 间歇性 DNS 故障

### Q21.1 — 基础定义

**问题：** 如何调查 intermittent DNS timeout？

**参考答案：**

先按 node、resolver、hostname、时间和 query type 分组，检查 resolver latency/error、cache hit、upstream health、packet loss 和 query volume。

### Q21.2 — 内部机制

**问题：** 解释 caching、recursive resolution、TTL、search domain、`ndots` 和 retry behaviour。

**参考答案：**

stub resolver 可能在多个 resolver 间 retry；search domain + `ndots` 会放大查询；TTL 决定 cache duration；recursive resolver 还依赖上游 authoritative path。

### Q21.3 — 生产场景

**问题：** 只有 2% 失败，而且集中在部分 nodes。哪些 hypotheses 更强？

**参考答案：**

node-local networking、conntrack、resolver config 或到 CoreDNS 的不均匀路径更可疑，而不是 authoritative DNS 数据本身。

### Q21.4 — 故障排查

**问题：** 如何测试 resolver health、CoreDNS/upstream、packet loss、conntrack pressure 和 `ndots` amplification？

**参考答案：**

从受影响 Pod 直接 `dig` cluster DNS 和 upstream，检查 `/etc/resolv.conf`、CoreDNS metrics/logs、node conntrack、UDP/TCP 53 capture 和 `ndots` 产生的额外 query。

### Q21.5 — Senior Trade-off / Edge Case

**问题：** 如何降低 DNS 成为系统性依赖，同时避免 stale discovery？

**参考答案：**

合理 scale/cache DNS，可考虑 NodeLocal DNS，减少不必要 search expansion，并监控 saturation；不要用过长 TTL 掩盖动态 service discovery。

---

## Q22 — HTTP 502 / 503 / 504

### Q22.1 — 基础定义

**问题：** 从运维角度解释 HTTP 502、503、504 的区别。

**参考答案：**

一般来说 502 表示代理拿到无效/失败的 upstream response；503 表示服务不可用或没有健康 capacity；504 表示代理等待 upstream 超时。实际要以生成该码的组件语义为准。

### Q22.2 — 内部机制

**问题：** 在 reverse proxy / load balancer 架构中，通常是谁生成这些状态码？

**参考答案：**

可能由 ALB、ingress、API gateway、service mesh proxy 或应用自己产生，所以必须先确认 emitter。

### Q22.3 — 生产场景

**问题：** 用户大量看到 504，但 backend CPU 很低。你会调查什么？

**参考答案：**

重点看 dependency latency、connection-pool exhaustion、network stall、queueing 和 timeout mismatch。低 CPU 并不代表服务健康。

### Q22.4 — 故障排查

**问题：** 如何结合 LB logs、upstream timing、retry、queue 和 dependency latency 找到来源？

**参考答案：**

对齐 LB/proxy access logs、upstream connect/response time、trace、queue depth、pool waits、retry counts 和 dependency timings，找出哪一 hop 消耗了 timeout budget。

### Q22.5 — Senior Trade-off / Edge Case

**问题：** timeout budget 和 error mapping 应如何设计，才能让状态码具有诊断价值？

**参考答案：**

外层 deadline 应大于合理的内层调用预算，并避免层层重试超过 caller deadline。错误码应尽量保留真实 failure phase，而不是全部映射成 generic 500。

---

## Q23 — Packet Loss

### Q23.1 — 基础定义

**问题：** 什么是 packet loss？它如何影响 TCP 应用？

**参考答案：**

packet loss 是数据包在到达目标前被丢弃。TCP 会通过 retransmission 和 congestion control 恢复，但会提高延迟并降低吞吐。

### Q23.2 — 内部机制

**问题：** 解释 retransmission、congestion control、latency inflation 和 throughput collapse。

**参考答案：**

丢包会触发 retransmit、duplicate ACK 和 congestion-window 收缩；即使小比例 loss，在长 RTT 或高吞吐连接上也可能显著影响性能。

### Q23.3 — 生产场景

**问题：** 应用 latency 上升但 CPU/memory 正常，什么证据会让 packet loss 成为主要 hypothesis？

**参考答案：**

如果同时看到 TCP retransmits、interface drops、recovery counters、path-specific failures 或某些 node/AZ 特别慢，packet loss 更可信。

### Q23.4 — 故障排查

**问题：** 如何合理使用 `ping`、`mtr`、`ss`、TCP retransmit metrics、packet capture 和 interface counters？

**参考答案：**

结合 `mtr`、interface counters、`ss -i`、TCP metrics 和定点 capture。不要把 ICMP loss 单独当作铁证，因为中间设备可能低优先级处理 ICMP。

### Q23.5 — Senior Trade-off / Edge Case

**问题：** 如果你不控制整条网络路径，如何区分 host、switch、overlay、WAN 和 cloud-provider loss？

**参考答案：**

通过端点和路径对比定位边界，并收集双向 packet/flow metrics、时间戳和 failing/healthy samples，形成足够证据交给网络或 provider 团队。

---

## Q24 — TLS Handshake

### Q24.1 — 基础定义

**问题：** 从工程实践角度解释 TLS handshake。

**参考答案：**

TLS 会协商协议与 cipher，验证服务器证书，执行 key agreement，派生对称 session keys，随后加密应用数据。现代 TLS 常结合 SNI、ALPN 和 session resumption。

### Q24.2 — 内部机制

**问题：** 说明 certificate validation、key agreement、session keys、SNI、ALPN 和 resumption。

**参考答案：**

certificate validation 检查 hostname 和 trust chain；SNI 用于选择虚拟主机/证书；ALPN 可协商 HTTP/2；ephemeral key exchange 提供 forward secrecy。

### Q24.3 — 生产场景

**问题：** 共享 LB 后只有一个 hostname 间歇 TLS failure，可能是什么原因？

**参考答案：**

可能是 SNI 选错证书、证书链不完整、hostname mismatch、某个 LB 节点配置不一致，或 backend/edge TLS 配置差异。

### Q24.4 — 故障排查

**问题：** 如何调查 certificate chain、SNI routing、clock skew、cipher/protocol mismatch 和 termination point？

**参考答案：**

使用 `openssl s_client`、`curl -v`、证书链检查、指定 SNI 测试、系统时间检查、LB config 和每个 termination point 的日志。

### Q24.5 — Senior Trade-off / Edge Case

**问题：** 多层平台中 TLS 应在哪里 terminate？re-encryption / mTLS 有什么 trade-off？

**参考答案：**

在清晰的 trust boundary 终止 TLS。mTLS/re-encryption 提高内部身份认证与机密性，但增加 certificate lifecycle、CPU 和 troubleshooting complexity。

---

# 04 — Kubernetes 基础与内部原理

## Q25 — Pod 创建生命周期

### Q25.1 — 基础定义

**问题：** 执行 `kubectl apply` 创建 Pod 后发生什么？

**参考答案：**

`kubectl` 把 desired state 发给 API server，经 admission 后持久化；scheduler 选择 node；kubelet 观察 Pod 并通过 CRI 创建容器，CNI/CSI 等准备网络与存储，然后 probes 决定 readiness。

### Q25.2 — 内部机制

**问题：** 依次解释 API server、admission、etcd、scheduler、kubelet、CRI、container runtime、CNI 的作用。

**参考答案：**

API server 是控制面入口；etcd 保存状态；admission 校验/修改对象；scheduler 做 binding；kubelet 负责 node reconciliation；CRI/CNI/CSI 分别对接 runtime、网络和存储。

### Q25.3 — 生产场景

**问题：** Pod object 已存在，但 container 完全没启动。可能卡在哪些阶段？

**参考答案：**

可能是 unscheduled、admission、image pull、volume setup、sandbox/CNI、runtime 或 kubelet/node 问题。此时应先看 Pod status/events，而不是应用日志。

### Q25.4 — 故障排查

**问题：** 如何用 events、scheduler data、kubelet logs、runtime state 和 CNI evidence 找到失败阶段？

**参考答案：**

沿生命周期逐级检查：scheduler event → Pod condition → kubelet log → CRI/container state → image → sandbox/CNI → volume。找到第一个不符合预期的阶段。

### Q25.5 — Senior Trade-off / Edge Case

**问题：** 生产集群中，你会优先让 lifecycle 的哪些阶段具备 HA 和 observability？为什么？

**参考答案：**

API/etcd、scheduler/controller、kubelet/runtime、CNI/CSI 都应有健康度和延迟/错误 observability。生产环境需要能区分“Pod Pending”背后的具体阶段。

---

## Q26 — Deployment → ReplicaSet → Pod

### Q26.1 — 基础定义

**问题：** 解释 Deployment、ReplicaSet 与 Pod 的关系。

**参考答案：**

Deployment 管理 ReplicaSets，ReplicaSet 维持期望 Pod 数。Deployment 提供 rollout/rollback 语义，ReplicaSet 专注 replica reconciliation。

### Q26.2 — 内部机制

**问题：** 为什么 Deployment 不直接管理 Pod？

**参考答案：**

每次 Pod template revision 会生成新的 ReplicaSet，Deployment 可以控制 old/new ReplicaSet 的比例、history 和 rollout strategy，因此职责分层更清晰。

### Q26.3 — 生产场景

**问题：** rollout 创建了新 ReplicaSet，但一直无法完全 scale。哪些机制控制 progress？

**参考答案：**

检查 readiness、scheduler capacity、PDB、maxSurge/maxUnavailable、quota、image pull、progressDeadline 等。

### Q26.4 — 故障排查

**问题：** 如何调查 maxSurge/maxUnavailable、readiness、PDB、quota、scheduling 和 rollout status？

**参考答案：**

使用 `kubectl rollout status`、Deployment/ReplicaSet/Pod events、PDB 状态、node allocatable、resource requests 和 probe results，判断是“新 Pod 起不来”还是“旧 Pod 退不掉”。

### Q26.5 — Senior Trade-off / Edge Case

**问题：** 关键 API 在 rollout speed、capacity overhead、availability 之间如何取舍？

**参考答案：**

关键服务通常保留 surge headroom 并降低允许 unavailable 的数量。更快 rollout 带来更大同时变更面；零 spare capacity 容易让 rollout 自己死锁。

---

## Q27 — Kubernetes Service 流量路径

### Q27.1 — 基础定义

**问题：** 什么是 Kubernetes Service？

**参考答案：**

Service 为动态 Pod endpoints 提供稳定的 virtual IP/name 和负载分发入口，后端通常由 selector/EndpointSlice 决定。

### Q27.2 — 内部机制

**问题：** 解释 Service VIP、EndpointSlice、kube-proxy 或 eBPF datapath 与 Pod IP 的关系。

**参考答案：**

EndpointSlices 保存实际 Pod endpoints；kube-proxy 通过 iptables/IPVS，或 eBPF dataplane 通过 BPF maps/programs，把 Service VIP 流量转发到 Pod IP。Service 本身不是一个普通 user-space proxy。

### Q27.3 — 生产场景

**问题：** Service DNS 正常解析，但 client 连不上。检查哪些层？

**参考答案：**

检查 EndpointSlices、Pod readiness/listener、network policy、service datapath、node routing 和 CNI connectivity。

### Q27.4 — 故障排查

**问题：** same-node Service traffic 正常，cross-node 失败。如何缩小到 CNI、routing、conntrack、policy 或 node networking？

**参考答案：**

same-node 成功而 cross-node 失败强烈指向 overlay/underlay routing、encapsulation、MTU、conntrack 或 node firewall。应在 source Pod/node 与 destination node/Pod 抓包定位丢失点。

### Q27.5 — Senior Trade-off / Edge Case

**问题：** 如果集群完全使用 eBPF 且没有传统 kube-proxy，诊断思路如何变化？

**参考答案：**

核心 reasoning 不变：endpoint → service translation → node path → Pod path；但工具从 iptables/IPVS 转向 CNI 提供的 BPF maps、flows 和 eBPF observability。

---

## Q28 — Pods Ready 但用户收到 503

### Q28.1 — 基础定义

**问题：** 所有 Pods 都是 Ready，但用户收到 503。你从哪里开始？

**参考答案：**

先确认是谁生成 503，然后从 LB/ingress target health、Service EndpointSlices、Pod version/node/AZ、应用和依赖逐层检查。

### Q28.2 — 内部机制

**问题：** 为什么 readiness 不能保证 end-to-end availability？

**参考答案：**

readiness 只验证配置的健康条件；Pod 可能能通过 `/ready`，但特定 route、auth、dependency 或数据分区仍失败。

### Q28.3 — 生产场景

**问题：** 只有一个 AZ 出现 503。哪些问题更可疑？

**参考答案：**

优先怀疑 zonal target registration、subnet/NAT、AZ-specific dependency、node/CNI 问题或 deployment 分布不均。

### Q28.4 — 故障排查

**问题：** 如何关联 ingress/LB targets、Service endpoints、app health、dependency health 和 zonal networking？

**参考答案：**

按 AZ 对比 LB target health、ingress logs、EndpointSlices、Pod placement/version、dependency endpoints 和 traces，找出 failing AZ 第一个异常 hop。

### Q28.5 — Senior Trade-off / Edge Case

**问题：** readiness 应如何设计，既反映 serving capability，又不过度依赖下游？

**参考答案：**

readiness 应便宜地验证关键 serving prerequisites，但不要依赖所有下游，否则一个 dependency 故障可能导致所有 Pod 同时 NotReady，放大事故。

---

## Q29 — CrashLoopBackOff

### Q29.1 — 基础定义

**问题：** `CrashLoopBackOff` 是什么意思？

**参考答案：**

`CrashLoopBackOff` 表示 container 反复退出，kubelet 在增加重启间隔。它只是症状，不是 root cause。

### Q29.2 — 内部机制

**问题：** 解释 restart policy、exponential backoff、container exit state，以及为什么 `--previous` logs 很重要。

**参考答案：**

kubelet 按 restart policy 重启，并在连续失败后增加 backoff。container status 保存 last termination reason/exit code；`kubectl logs --previous` 可看到上一实例日志。

### Q29.3 — 生产场景

**问题：** Pod 能正常启动约 20 秒后持续 crash。你会考虑哪些原因？

**参考答案：**

启动 20 秒后才 crash 更像 runtime exception、liveness failure、dependency/config 问题、OOM 或后台任务触发，而不是最初镜像启动失败。

### Q29.4 — 故障排查

**问题：** 如何检查 events、exit code、OOMKilled、probes、config、secrets、dependencies 和应用 startup/shutdown？

**参考答案：**

先看 `kubectl describe`、events、last state、exit code、`logs --previous`、OOMKilled、probe history、mounted config/secrets 和依赖连通性，再结合应用 metrics。

### Q29.5 — Senior Trade-off / Edge Case

**问题：** 什么时候暂时放宽 liveness probe 是合理 mitigation？什么时候会掩盖真正故障？

**参考答案：**

如果 probe 明显配置错误而应用其实健康，暂时放宽可恢复流量；若应用真的 hang/crash，放宽 probe 只会延迟检测。事故后应修正 probe semantics 和 shutdown/startup behaviour。

---

## Q30 — Pending Pod

### Q30.1 — 基础定义

**问题：** Pod 长时间处于 Pending 说明什么？

**参考答案：**

`Pending` 表示 Pod 尚未进入正常 running container 状态，最常见是未能 schedule，或在等待 storage/network/runtime setup。

### Q30.2 — 内部机制

**问题：** 解释 resource requests、affinity、taints/tolerations、PVC binding、quota、admission 等如何导致 Pending。

**参考答案：**

可能因为资源不足、affinity/anti-affinity、taints、quota、PVC/StorageClass、topology constraint、admission 或 node 不可用。

### Q30.3 — 生产场景

**问题：** 只有新 Pod Pending，而已有 workload 正常运行。优先怀疑什么？

**参考答案：**

更像新容量不足、quota 耗尽、scheduling rule 最近变化、storage provisioning 故障、admission change 或可用 node 减少。

### Q30.4 — 故障排查

**问题：** 如何使用 scheduler events、node allocatable、quota、storage state 和 affinity rules 找到 blocker？

**参考答案：**

先读 Pod events/scheduler reason，再对比 node allocatable 与 requests、taints/tolerations、affinity、ResourceQuota、PVC/StorageClass 和 admission logs。

### Q30.5 — Senior Trade-off / Edge Case

**问题：** 如何设计 capacity headroom 和 scheduling policy，避免单个 placement constraint 造成大面积 outage？

**参考答案：**

保留 N+1/headroom，避免过度 rigid anti-affinity，监控 unschedulable reasons，并在上线前验证 PDB、topology、capacity 的组合能否在 node/AZ failure 时仍调度。

---

## Q31 — Kubernetes DNS

### Q31.1 — 基础定义

**问题：** Kubernetes 集群内 DNS 如何工作？

**参考答案：**

Pod 通常通过 cluster DNS（常见 CoreDNS）解析 Service 名称和外部域名。Service records 由 Kubernetes API 数据生成，外部查询可转发到 upstream resolver。

### Q31.2 — 内部机制

**问题：** 解释 CoreDNS、Service discovery、search domains、`ndots`、caching 和 upstream resolution。

**参考答案：**

`/etc/resolv.conf` 的 search domains 和 `ndots` 会影响查询展开；CoreDNS 可 cache、forward、rewrite，Service 可对应 ClusterIP 或 headless endpoints。

### Q31.3 — 生产场景

**问题：** 只有部分 nodes 上的 Pods 间歇无法解析 Service。你会怎么判断？

**参考答案：**

故障集中在 node subset 时，更可疑的是 node-local networking、conntrack、resolver config 或到 CoreDNS 的路径，而不是全局 DNS record。

### Q31.4 — 故障排查

**问题：** 如何测试 CoreDNS、node networking、conntrack、resolver config、packet loss 和 query amplification？

**参考答案：**

从 affected Pod 直接查询 cluster DNS/upstream，检查 `/etc/resolv.conf`、CoreDNS latency/error metrics、node conntrack、UDP/TCP 53 packet capture 和 `ndots` 放大量。

### Q31.5 — Senior Trade-off / Edge Case

**问题：** 如何提高大规模集群 DNS resilience，同时避免 stale data？

**参考答案：**

可水平扩容 CoreDNS、启用合理 caching/NodeLocal DNS、减少无谓 search expansion，并监控 saturation。TTL 不应过长到妨碍动态 service discovery。

---

## Q32 — etcd 不可用

### Q32.1 — 基础定义

**问题：** etcd 在 Kubernetes 中负责什么？

**参考答案：**

etcd 是 Kubernetes control-plane 的持久状态存储，保存 API objects 和控制面协调所需数据。

### Q32.2 — 内部机制

**问题：** etcd unavailable 时，reads、writes、scheduling、controllers 和已有 workloads 分别会怎样？

**参考答案：**

etcd 不可用时 API writes 和许多 fresh reads 会失败，scheduler/controller 无法持久化新决策；但已经运行的 Pods 和 data-plane traffic 往往还能继续。

### Q32.3 — 生产场景

**问题：** 应用仍在 serving，但无法创建新 Pods。如何确认 etcd 是 control-plane bottleneck？

**参考答案：**

检查 API server 的 etcd error/latency、对象创建更新失败、scheduler/controller 写入失败，同时已有 workloads 正常 serving，这种组合很典型。

### Q32.4 — 故障排查

**问题：** 如何调查 quorum、disk latency、network partition、certificate、resource pressure 和 member health？

**参考答案：**

查看 etcd member/endpoint health、quorum、peer/client 网络、disk fsync latency、CPU/memory、certificate、DB size 和 alarms。不要在事故中随意 remove members。

### Q32.5 — Senior Trade-off / Edge Case

**问题：** 生产 etcd 应如何设计 backup、restore、quorum 和 failure domain？

**参考答案：**

使用奇数成员并跨 failure domains，配合低延迟 durable disk、定期 snapshots、certificate monitoring 和真正演练过的 restore/member replacement runbook。

---

# 05 — Kubernetes 生产故障排查

## Q33 — Deployment 后错误率上涨

### Q33.1 — 基础定义

**问题：** 部署 5 分钟后 error rate 从 0.1% 升到 8%。第一步做什么？

**参考答案：**

先确认 customer impact、时间线和与 rollout 的相关性，按 old/new version 对比 metrics，并快速判断 safe rollback/traffic shift 是否可行。

### Q33.2 — 内部机制

**问题：** 如何关联 rollout timeline、version、configuration、traffic 和 dependency changes？

**参考答案：**

把 deployment revision、config/secret/feature flag、schema migration、traffic split、dependency health 和版本标签 telemetry 对齐。

### Q33.3 — 生产场景

**问题：** 只有新 ReplicaSet 错误率高，而旧 Pods 健康。立即 mitigation 是什么？

**参考答案：**

停止 rollout，并在 rollback 安全的前提下恢复旧版本或把流量移出新 ReplicaSet。同时保留足够 telemetry/少量 failing instance 用于后续分析。

### Q33.4 — 故障排查

**问题：** 如果 rollback 后问题仍存在，如何判断 schema/cache/external dependency/irreversible side effects？

**参考答案：**

检查 DB migration、cache/state mutation、queue backlog、feature flags、external API change、config 以及同时发生的 dependency failure。rollback binary 不等于 rollback state。

### Q33.5 — Senior Trade-off / Edge Case

**问题：** 如何制定 rollback criteria 和 release guardrails，让决策快速且不依赖个人直觉？

**参考答案：**

预先定义 error/latency/SLO burn 自动 abort 阈值，使用 canary/progressive delivery，确保 schema backward compatibility，并让所有 telemetry 带 version 标签。

---

## Q34 — 单个 Pod 明显变慢

### Q34.1 — 基础定义

**问题：** 10 个 Pods 中只有一个 latency 是其他 Pod 的 3 倍。可能是什么原因？

**参考答案：**

先比较 slow Pod 与 healthy peers 的 node、CPU throttling、memory/GC、network、disk、connection pools、cache、version/config 和 dependency endpoints。

### Q34.2 — 内部机制

**问题：** 解释 node locality、CPU throttling、GC、network path、disk、connection pool 和 cache warmth 对单实例的影响。

**参考答案：**

Pod 虽然规格相同，但共享不同 node kernel/NIC/disk/cgroup 条件，因此 node-local contention、noisy neighbour 或 CNI 问题可只影响一个实例。

### Q34.3 — 生产场景

**问题：** 把这个 Pod reschedule 到另一 node 后恢复正常。这说明什么？

**参考答案：**

node-local root cause 概率显著上升，应在 drain/reboot 前尽量保存该 node 的网络、kernel、storage 和 cgroup 证据。

### Q34.4 — 故障排查

**问题：** 如何检查 node metrics、cgroup throttling、network counters、storage latency 和 noisy-neighbour evidence？

**参考答案：**

检查 per-container throttling、node CPU modes、disk latency、NIC drops、conntrack、kernel logs、CNI state 和 dependency timings，并与健康 node 同负载对比。

### Q34.5 — Senior Trade-off / Edge Case

**问题：** 如何自动检测并隔离 single-instance degradation？

**参考答案：**

建立 per-instance tail latency/outlier detection、readiness/traffic removal、node health remediation 和自动 canary protection，同时限制自动 drain/reboot 的速率。

---

## Q35 — HPA 不扩容

### Q35.1 — 基础定义

**问题：** CPU 很高但 HPA 没有 scale。先检查什么？

**参考答案：**

查看 `kubectl describe hpa`、metric availability、target type/value、resource requests、min/max replicas、scaling policies/stabilization，以及新 Pods 是否能 schedule。

### Q35.2 — 内部机制

**问题：** 解释 HPA 如何获取 metrics、计算 desired replicas、使用 stabilization，并与 requests/limits 交互。

**参考答案：**

CPU HPA 常按实际使用量相对 CPU request 计算，不等于 node 的总 CPU 百分比。controller 根据 current/target ratio 计算 desired replicas，并受 behaviour/stabilization 约束。

### Q35.3 — 生产场景

**问题：** `top` 显示 90% CPU，但 HPA 只看到 45% utilization。为什么？

**参考答案：**

`top` 可能展示绝对/容器 CPU，而 HPA 使用平均 usage/request；如果 requests 设得偏大、metric 缺失或 Pod 尚未 ready，结果会不同。

### Q35.4 — 故障排查

**问题：** 如何检查 metrics-server/custom metrics、resource requests、HPA events、maxReplicas、cooldown 和 pending capacity？

**参考答案：**

检查 Metrics API 时间戳和数值、resource requests、HPA conditions/events、maxReplicas、scale policies、Pending Pods 和 cluster capacity。

### Q35.5 — Senior Trade-off / Edge Case

**问题：** 什么时候 CPU 是错误的 scaling signal？应该换成什么？

**参考答案：**

queue worker 更适合 queue depth/age；I/O-bound API 可能更适合 concurrency、RPS 或 latency。选择最能代表 demand/saturation 的指标，而不是默认 CPU。

---

## Q36 — JVM Heap 正常但 Pod OOMKilled

### Q36.1 — 基础定义

**问题：** Java service 被 OOMKilled，但 JVM heap 看起来正常。为什么？

**参考答案：**

Kubernetes OOM 是依据 cgroup total memory，不是 `-Xmx`。非 heap/native memory 完全可能把 container limit 吃满。

### Q36.2 — 内部机制

**问题：** 解释 container memory 中 heap 以外的 metaspace、direct buffer、thread stack、native library、mmap 和 page cache。

**参考答案：**

除 heap 外还有 metaspace、code cache、direct buffers、每线程 stack、JNI/native libs、allocator overhead、mmap 和 file/page cache。

### Q36.3 — 生产场景

**问题：** kernel 报 cgroup OOM，但 heap dump 没 leak。下一步查什么？

**参考答案：**

检查 NMT、thread count、direct buffers、metaspace/native libs、memory mappings、cgroup `memory.stat` 和 working set。

### Q36.4 — 故障排查

**问题：** 如何关联 cgroup memory、Native Memory Tracking、`/proc`、thread count、direct buffer 和 limits？

**参考答案：**

用 `jcmd VM.native_memory`、`/proc/<pid>/smaps`、cgroup stats、thread dumps、direct-buffer metrics 和历史趋势，找出哪类内存增长。

### Q36.5 — Senior Trade-off / Edge Case

**问题：** 如何为 JVM/container 做 memory sizing 与 guardrails？

**参考答案：**

为 `-Xmx` 留足 native/headroom，限制 thread/direct memory，监控 cgroup headroom，并用真实并发 load test 验证，而不是只根据 heap sizing。

---

## Q37 — Node NotReady

### Q37.1 — 基础定义

**问题：** 什么是 `NodeNotReady`？

**参考答案：**

`NodeNotReady` 表示 control plane 在预期时间内没有收到健康 node status/lease，或者 node condition 表明节点不可正常使用。

### Q37.2 — 内部机制

**问题：** 解释 kubelet heartbeat/lease、node conditions 与 controller eviction behaviour。

**参考答案：**

kubelet 周期更新 status/lease；node controller 根据 freshness 和 conditions 添加 taint，并在策略允许时驱逐 workloads。

### Q37.3 — 生产场景

**问题：** peak traffic 时 node NotReady，但 SSH 仍能连。你会考虑什么？

**参考答案：**

host 还活着，因此优先看 kubelet/runtime、API server connectivity、CPU starvation、disk/inode/PID pressure、certificate/auth、node network/CNI。

### Q37.4 — 故障排查

**问题：** 如何在不立刻 reboot 的情况下调查 kubelet、runtime、disk/PID pressure、network 和 API reachability？

**参考答案：**

检查 node conditions/events、kubelet/containerd logs、systemd、disk/inodes、PID count、cgroup pressure、route/DNS、API endpoint reachability，然后决定 cordon/drain/restart。

### Q37.5 — Senior Trade-off / Edge Case

**问题：** 怎样的 automated node remediation 才安全？

**参考答案：**

自动 remediation 必须有 rate limit、cluster capacity/quorum awareness、失败熔断和人工 override。共享网络故障时不能让所有 nodes 自动重启。

---

## Q38 — Cross-node Pod Traffic 故障

### Q38.1 — 基础定义

**问题：** same-node Pod traffic 正常，但 cross-node 间歇失败。这说明什么？

**参考答案：**

这强烈指向 inter-node dataplane：CNI overlay/underlay routing、encapsulation、MTU、node firewall、conntrack 或 physical network。

### Q38.2 — 内部机制

**问题：** 解释 CNI 下跨 node packet path 中 routing、encapsulation、NAT/policy 可能出错的位置。

**参考答案：**

packet 可能经过 veth → CNI/BPF/iptables → host route/tunnel → NIC → underlay → destination node → reverse path。任一阶段都可能丢包。

### Q38.3 — 生产场景

**问题：** 故障集中在某两个 worker 之间。如何进一步缩小范围？

**参考答案：**

比较这两个 node 与健康 node pair 的 routes、MTU、tunnel endpoints、CNI/BPF rules、NIC counters 和 conntrack，可以快速定位 path-specific 差异。

### Q38.4 — 故障排查

**问题：** 如何使用 route、CNI state、MTU、conntrack、packet capture、interface drops 和 NetworkPolicy 诊断？

**参考答案：**

逐点抓包 source Pod/source node/destination node/destination Pod，同时检查 route、CNI state、MTU、fragmentation、conntrack、policy、interface errors 和 kernel logs。

### Q38.5 — Senior Trade-off / Edge Case

**问题：** 哪些 cluster-network architecture 与 observability 设计可以减少这类故障？

**参考答案：**

保持 underlay/overlay MTU 一致，监控 packet drops/conntrack/tunnel health，选择具备 flow visibility 的 CNI，并持续做 cross-node synthetic checks。

---

## Q39 — Rollout 卡在 8/10

### Q39.1 — 基础定义

**问题：** Deployment rollout 卡在 8/10 replicas。检查什么？

**参考答案：**

查看 Deployment/ReplicaSet/Pod conditions/events、new/old replica count、readiness、unschedulable reason、PDB、surge/unavailable、quota 和 image pulls。

### Q39.2 — 内部机制

**问题：** 解释 readiness、scheduling、PDB、maxSurge/maxUnavailable、quota 如何影响 progress。

**参考答案：**

rollout 需要既能创建足够新 Pod，又能在 availability policy 允许下移除旧 Pod。容量和策略组合不当会让两边都无法前进。

### Q39.3 — 生产场景

**问题：** 两个新 Pods Pending，而旧 Pods 因 PDB 不能终止，可能发生了什么？

**参考答案：**

很可能 rollout 需要额外 surge capacity，但 scheduler 没地方放新 Pod，同时 PDB/availability rule 又不允许先删旧 Pod，形成 policy/capacity deadlock。

### Q39.4 — 故障排查

**问题：** 如何证明 blocker 是 capacity、PDB、affinity、quota、image pull 还是 probe？

**参考答案：**

读 scheduler events、PDB disruptionsAllowed、node allocatable vs requests、affinity、quota、image/probe status，找到最直接的 blocking condition。

### Q39.5 — Senior Trade-off / Edge Case

**问题：** 如何设计 rollout policy，避免 cluster spare capacity 很少时 deadlock？

**参考答案：**

保留 rollout headroom，避免 maxSurge=0 与严格 PDB/anti-affinity 组合，在 node/AZ failure 以及低 spare capacity 情况下提前演练 rollout。

---

## Q40 — Control Plane 健康但 Workload 不健康

### Q40.1 — 基础定义

**问题：** Kubernetes control-plane metrics 正常，但 application availability 在下降。如何缩小 fault domain？

**参考答案：**

从用户 symptom outside-in：edge/ingress → Service/endpoints → Pods → node/network/storage → app → dependencies。control plane healthy 只证明 Kubernetes 管理面没有明显故障。

### Q40.2 — 内部机制

**问题：** 为什么 API server healthy 不代表 node、network、storage、ingress 或 application healthy？

**参考答案：**

API server/etcd 是 management plane；data plane、CNI、CSI、DNS、ingress、node kernel 和应用完全可以独立失败。

### Q40.3 — 生产场景

**问题：** 只有一个 service 受影响，同 node 上其他 workload 健康。这说明什么？

**参考答案：**

优先聚焦该 service 的 version/config/endpoints/dependencies，而不是把整个 cluster 当 root cause。

### Q40.4 — 故障排查

**问题：** 如何从 external symptom 逐层走到 ingress、Service、Pod、node、dependency 和 application evidence？

**参考答案：**

用 synthetic request、ingress logs、traces、EndpointSlices、per-Pod metrics、node placement 和 dependency timing，每一步寻找 failing/healthy path 第一个差异。

### Q40.5 — Senior Trade-off / Edge Case

**问题：** 如何设计 layered SLO 与 synthetic checks，自动区分 platform health 和 workload health？

**参考答案：**

分别定义 platform SLO、service SLO 和 business-journey SLO；配合 end-to-end synthetic transaction，避免“control plane green”被误解为用户正常。

---

# 06 — AWS / 云

## Q41 — ALB 到应用 Timeout

### Q41.1 — 基础定义

**问题：** 请求到达 ALB，但似乎没有到 application。你检查什么？

**参考答案：**

先确认 listener/rule 命中、target registration/health、backend port、SG reachability、subnet route，然后检查应用是否真的 listening 和是否收到请求。

### Q41.2 — 内部机制

**问题：** 解释 target group、health check、listener、security group、routing、subnet 和 backend connection flow。

**参考答案：**

ALB 接受 client connection，匹配 listener rules，选择 target，再建立/复用 backend connection。target group、subnet、route、SG 和应用 listener 都参与。

### Q41.3 — 生产场景

**问题：** targets healthy，但请求仍间歇 timeout。还可能是什么？

**参考答案：**

health check 很简单而真实请求可能慢，因此还要看 dependency latency、connection pool、backend reset、uneven target、network/NAT 和 timeout mismatch。

### Q41.4 — 故障排查

**问题：** 如何关联 ALB access logs、target response time、SG/NACL、socket 和 dependency latency？

**参考答案：**

结合 ALB access logs、`target_response_time`、target health reason、VPC Flow Logs、SG/NACL、backend `ss`/logs、trace 和 downstream metrics。

### Q41.5 — Senior Trade-off / Edge Case

**问题：** 如何设置 timeout、health check 和 connection management，减少模糊 ALB failure？

**参考答案：**

health check 应代表基本 serving ability；per-hop timeout 要一致；使用 keepalive/pooling，并把 target-level latency/error 暴露出来，方便识别单 target 问题。

---

## Q42 — Security Group 与 NACL

### Q42.1 — 基础定义

**问题：** Security Group 和 NACL 的区别是什么？

**参考答案：**

SG 是绑定到 ENI/resource 的 stateful firewall，只包含 allow；NACL 是 subnet-level stateless filter，可按顺序 allow/deny。

### Q42.2 — 内部机制

**问题：** 哪个是 stateful？哪个是 stateless？规则如何评估？

**参考答案：**

SG 对允许连接自动放行 return traffic；NACL inbound/outbound 分别评估，因此 return ephemeral ports 也必须匹配规则。

### Q42.3 — 生产场景

**问题：** 相同 SG 下，一个 subnet 正常，一个 subnet 失败。可能为什么？

**参考答案：**

优先比较两个 subnet 的 route table、NACL association、NAT/IGW path、network appliance，因为 SG 相同而 subnet 级配置不同。

### Q42.4 — 故障排查

**问题：** 如何证明问题来自 NACL、route、SG、ephemeral port 还是其他层？

**参考答案：**

可结合 VPC Flow Logs、Reachability Analyzer、route/NACL/SG review 和 packet test，尤其确认 stateless NACL 的双向端口。

### Q42.5 — Senior Trade-off / Edge Case

**问题：** 怎样管理网络 policy 才能兼顾 least privilege 与降低 outage 风险？

**参考答案：**

以 SG 为主要 workload policy，NACL 尽量保持简单；全部 as code、review、复用 SG references/prefix list，并避免长期保留事故中的宽泛规则。

---

## Q43 — Multi-AZ 设计

### Q43.1 — 基础定义

**问题：** Multi-AZ 到底保护你免受什么故障？

**参考答案：**

Multi-AZ 主要防单 AZ 层面的基础设施故障，通过把 compute/state/dependencies 分散到独立 failure domains。

### Q43.2 — 内部机制

**问题：** 它不能保护哪些 failure classes？

**参考答案：**

它不能自动防 region outage、应用 bug、共享配置错误、全球服务故障、单一数据库/第三方依赖或容量不足。

### Q43.3 — 生产场景

**问题：** 应用部署在三个 AZ，但一个 AZ down 后仍 outage。可能是什么设计错误？

**参考答案：**

可能剩余 AZ 没 N+1 capacity、DB/quorum 仍依赖故障 AZ、zonal NAT/egress 单点、LB distribution 不均或 stateful service 不能 failover。

### Q43.4 — 故障排查

**问题：** 如何调查 remaining capacity、zonal load balancing、NAT、stateful dependency、quorum 和 cross-AZ traffic？

**参考答案：**

模拟删除该 AZ，检查 autoscaling headroom、target health、DB failover/quorum、NAT/routes、queue/storage placement 和按 AZ 的 SLO/error。

### Q43.5 — Senior Trade-off / Edge Case

**问题：** 哪些 capacity/dependency rules 能确保系统真正 survive AZ loss？

**参考答案：**

至少按 N+1 规划 capacity，跨 AZ 放置 stateful quorum，消除隐藏 zonal dependency，并通过 game day 实测，而不是只看架构图有三个 AZ。

---

## Q44 — AWS API Throttling

### Q44.1 — 基础定义

**问题：** 什么是 AWS API throttling？

**参考答案：**

当请求速率超过 AWS service/account/resource/region 限制时会被 throttle，客户端应采用 bounded exponential backoff + jitter。

### Q44.2 — 内部机制

**问题：** 解释 rate/burst limit、exponential backoff、jitter 和 token-bucket 风格行为。

**参考答案：**

许多 control-plane API 有 steady rate 和 burst capacity；多个 client 同步 retry 会让 throttle 更严重，所以并发控制与 jitter 很关键。

### Q44.3 — 生产场景

**问题：** autoscaling 后 automation fleet 突然大量 throttling。怎么办？

**参考答案：**

先压低非必要 calls、限制 concurrency、停止 retry storm、batch/cache 稳定 metadata，并找出具体 service/action/caller。

### Q44.4 — 故障排查

**问题：** 如何找出 offending API/client，并区分 account/region/service quota？

**参考答案：**

通过 SDK error、CloudTrail、client-side per-action metrics、service quota 文档和 account/region 维度确认限制来源。

### Q44.5 — Senior Trade-off / Edge Case

**问题：** 什么时候应该申请 quota increase，什么时候应该 redesign？

**参考答案：**

只有持续合法需求确实超过合理 limit 时申请 quota；大量 polling、重复 Describe/List、无缓存和无 event-driven design 应优先重构。

---

## Q45 — S3 Consistency

### Q45.1 — 基础定义

**问题：** S3 提供什么 consistency guarantee？

**参考答案：**

S3 对对象 PUT/DELETE 和 LIST 提供 strong consistency；成功写入后，后续读和列表应能看到最新结果。

### Q45.2 — 内部机制

**问题：** 为什么应用仍可能观察到“看似不一致”？

**参考答案：**

完整应用还包含 CDN/cache、async replication、event delivery、metadata DB、race conditions，所以 end-to-end 并不因为 S3 强一致就自动强一致。

### Q45.3 — 生产场景

**问题：** producer 写完对象后 consumer 说对象不存在，除了 S3 consistency 还可能是什么？

**参考答案：**

检查 key/bucket/region/account/version、writer 是否真的成功、IAM/KMS、缓存、事件顺序、key transformation 和 consumer 自己的 retry logic。

### Q45.4 — 故障排查

**问题：** 如何检查 cache、versioning、IAM、replication、event timing 和 application race？

**参考答案：**

追踪 object key/version/ETag，从 producer 到 consumer 对齐日志，必要时看 CloudTrail data events、replication status、cache layer 和 event timestamp。

### Q45.5 — Senior Trade-off / Edge Case

**问题：** 如何设计 idempotent object workflow，避免 retry/duplicate event 造成 business inconsistency？

**参考答案：**

使用 deterministic keys、version/ETag、idempotent consumers、duplicate-event handling，并把 S3 event 当 notification 而不是 exactly-once transaction。

---

## Q46 — IAM AccessDenied

### Q46.1 — 基础定义

**问题：** IAM policy 看起来 allow，但请求仍 AccessDenied。哪些东西能覆盖 allow？

**参考答案：**

任何 applicable explicit deny 都会覆盖 allow；SCP、permission boundary、session policy、resource policy condition、KMS key policy 等都可能限制最终权限。

### Q46.2 — 内部机制

**问题：** 解释 identity policy、resource policy、SCP、permission boundary、session policy、explicit deny 和 KMS policy。

**参考答案：**

identity policy 描述 principal 权限；resource policy 从资源侧授权；SCP/boundary 限制最大权限集合；session policy 进一步收窄；explicit deny 永远优先。

### Q46.3 — 生产场景

**问题：** 只有一个 assumed role 失败，而相似 role 正常。应该比较什么？

**参考答案：**

比较 role policy、session tags/policy、permission boundary、principal ARN/source identity、SCP/account、resource policy 和 KMS key policy/grants。

### Q46.4 — 故障排查

**问题：** 如何使用 CloudTrail、policy simulation、role session context 和 resource/KMS policy 定位？

**参考答案：**

用 CloudTrail 找精确 principal/action/resource/context，再结合 Access Analyzer/policy simulator、授权错误解码（支持时）和所有 policy layers。

### Q46.5 — Senior Trade-off / Edge Case

**问题：** 怎样设计 IAM 才既 least privilege 又容易诊断？

**参考答案：**

保持 role 单一职责、policy as code、清晰 condition/tag 约定、集中管理 SCP/boundary，并确保 CloudTrail 可检索。巨型 wildcard role 既不安全也难 debug。

---

## Q47 — AWS Region Degradation 与 Failover

### Q47.1 — 基础定义

**问题：** AWS primary region 部分 degraded。什么时候应该 fail over？

**参考答案：**

当 primary 的预期 customer harm 大于 failover 本身的风险与成本时才切换。依据用户 SLO、故障持续时间、dependency status、secondary readiness 和 data correctness，而不是只看 AWS status page。

### Q47.2 — 内部机制

**问题：** regional failover 前需要哪些 technical/business signals？

**参考答案：**

需要确认 RTO/RPO、secondary capacity、replication lag、write authority、traffic control、credentials/secrets 和第三方依赖。

### Q47.3 — 生产场景

**问题：** 部分 dependency 正常、部分 degraded。如何判断 failover 会改善还是恶化？

**参考答案：**

建立 primary 与 secondary 的完整 dependency graph。如果 secondary 也依赖同一 failing global service，或数据严重滞后，failover 可能更糟。

### Q47.4 — 故障排查

**问题：** 如何验证 secondary region capacity、data currency、DNS/traffic controls 和 external dependencies？

**参考答案：**

先做 synthetic transaction，确认 capacity、replication、DNS/route、auth、external integrations、observability，再按阶段 shift traffic，并准备 failback。

### Q47.5 — Senior Trade-off / Edge Case

**问题：** 应该预先建立怎样的 RTO/RPO、game day 和 decision authority？

**参考答案：**

提前定义触发条件、决策人、RTO/RPO、runbook，并定期演练。没有经过真实 game day 的 failover capability 不能视为可靠。

---

## Q48 — AWS Cost Anomaly

### Q48.1 — 基础定义

**问题：** AWS cost 一夜上涨 40%，但已知 traffic 没增长。先从哪查？

**参考答案：**

先用 Cost Explorer/CUR 找出 delta 出现在哪个 account/service/region/usage type/resource/tag 和具体时间段，再回到技术事件。

### Q48.2 — 内部机制

**问题：** 如何按 service、account、region、tag、usage type 和 time 拆成本？

**参考答案：**

成本 = usage × price。即使流量平，也可能因为 instance size/count、data transfer、logs、storage、NAT、commitment coverage 或资源效率变化而上涨。

### Q48.3 — 生产场景

**问题：** compute cost 增加但 request volume 平。技术上可能是什么？

**参考答案：**

可能 scale-in 失效、autoscaling stuck high、实例规格变大、runaway batch job、重复环境、CPU 效率下降，或者真正增加的是 data/logging 而非 compute request。

### Q48.4 — 故障排查

**问题：** 如何关联 autoscaling、runaway jobs、data transfer、NAT、logs 和 storage growth？

**参考答案：**

对齐 autoscaling history、CloudTrail/change events、resource inventory、NAT/data transfer metrics、log ingestion 和 storage lifecycle。

### Q48.5 — Senior Trade-off / Edge Case

**问题：** 如何设计 FinOps guardrails，同时不阻塞合法 scaling？

**参考答案：**

使用 budget/anomaly alert、mandatory tags、autoscaling bounds、log retention/lifecycle、cost review，但优先告警/审批而不是在事故中硬阻止扩容。

---

# 07 — Terraform / Infrastructure as Code

## Q49 — Terraform State

### Q49.1 — 基础定义

**问题：** Terraform 为什么需要 state？

**参考答案：**

state 把 Terraform 配置中的 resource address 映射到真实 remote object，并保存 planning/lifecycle 所需属性。

### Q49.2 — 内部机制

**问题：** 解释 resource address、remote object、dependency 和 state mapping。

**参考答案：**

单靠 config 往往无法可靠重新发现 provider-generated IDs、对象身份和 prior relationships，因此 state 是 Terraform 的 ownership/memory layer。

### Q49.3 — 生产场景

**问题：** 两个 teams 用不同 state 管理同一套 infrastructure，会发生什么？

**参考答案：**

两个 state 都认为自己拥有同一资源时，会产生冲突 update/destroy、重复创建和 state overwrite 风险。应立即停止并行 applies。

### Q49.4 — 故障排查

**问题：** 如何安全检测 ownership conflict 并 consolidate/import？

**参考答案：**

先备份 states，找 canonical owner，对比 remote IDs 和 plans，再使用 import、`state mv`、`state rm` 等受控方式重建单一 ownership。

### Q49.5 — Senior Trade-off / Edge Case

**问题：** 大型组织如何划分 state boundary，平衡 blast radius、dependency 和 team autonomy？

**参考答案：**

边界应贴合 team ownership 和 blast radius。太大导致耦合和慢 plan，太小导致 remote-state dependency 泛滥；通过明确 outputs/interfaces 协作。

---

## Q50 — Terraform State 丢失

### Q50.1 — 基础定义

**问题：** Terraform state 丢失会怎样？

**参考答案：**

Terraform 会失去 config address 与真实资源之间的 ownership mapping；空 state 下 plan 可能试图重新创建或执行错误 reconciliation。

### Q50.2 — 内部机制

**问题：** 为什么只重新运行相同 configuration 并不能安全恢复？

**参考答案：**

相同 config 并不知道现有对象的真实 ID、provider-generated attributes 和历史关系，因此不能把 config 当 backup。

### Q50.3 — 生产场景

**问题：** state 被删但 infrastructure 还在。第一步做什么？

**参考答案：**

立即 freeze applies，保护现有 infrastructure，寻找 remote backend 的版本/backup，并固定当前 config/provider versions；不要对 production empty state 执行 apply。

### Q50.4 — 故障排查

**问题：** 如何从 remote-state version/backup 恢复，或通过 import 重建？

**参考答案：**

优先恢复最近有效 state，随后 plan 验证。若必须重建，按 resource 逐个 import，核对 address/ID，处理 drift，直到 plan 无意外 destructive action。

### Q50.5 — Senior Trade-off / Edge Case

**问题：** 哪些 controls 能让永久 state loss 极难发生？

**参考答案：**

使用 encrypted/versioned remote state、locking、least-privilege、备份和 restore drill。把 state backend 当生产数据，而不是临时缓存。

---

## Q51 — Infrastructure Drift

### Q51.1 — 基础定义

**问题：** 什么是 Terraform drift？

**参考答案：**

drift 是 Terraform desired/state 与真实 infrastructure 不一致，常由手工 change、外部 automation 或 provider-side change 造成。

### Q51.2 — 内部机制

**问题：** plan/refresh 如何发现 drift？哪些 drift 可能是 intentional？

**参考答案：**

provider read/refresh 获取 remote current state，再与 config 比较。部分 computed/default changes 不一定是业务问题，需要理解 provider semantics。

### Q51.3 — 生产场景

**问题：** 工程师在 outage 中通过 Console 紧急修改资源，事后应怎么办？

**参考答案：**

先记录 emergency change 和原因，稳定事故后马上 reconcile：判断最终 desired state 是否应该保留这项修改。

### Q51.4 — 故障排查

**问题：** 如何决定 revert、codify、import 还是 redesign？

**参考答案：**

运行 plan，理解依赖和 exact diff，然后通过 code、import/state 操作或 Terraform revert 恢复一致性。避免直接手工编辑 state。

### Q51.5 — Senior Trade-off / Edge Case

**问题：** 怎样允许 break-glass changes，同时不让 unmanaged infra 常态化？

**参考答案：**

建立有审计的 break-glass 流程、post-incident reconciliation deadline、continuous drift detection 和明确 ownership。

---

## Q52 — `terraform plan` 内部过程

### Q52.1 — 基础定义

**问题：** `terraform plan` 到底做什么？

**参考答案：**

`plan` 评估 configuration，读取 state/remote objects，构建 dependency graph，计算 actual/known state 到 desired state 的 proposed actions。

### Q52.2 — 内部机制

**问题：** 解释 config evaluation、provider reads、state refresh、dependency graph、diff 和 unknown values。

**参考答案：**

provider schema 决定哪些 attributes 可更新、哪些 ForceNew；apply-time 才知道的值会保持 unknown；resource graph 决定顺序和 dependency。

### Q52.3 — 生产场景

**问题：** plan 显示 critical resource 将被 replace。怎么办？

**参考答案：**

立即停止 apply，找出触发 replacement 的 exact attribute，并检查 provider/version、state drift、address change、lifecycle rules 和 data source。

### Q52.4 — 故障排查

**问题：** 如何检查 provider schema、lifecycle、changed attributes、state 和 dependencies？

**参考答案：**

使用 saved plan + `terraform show`、state inspection、git diff、provider docs/schema、lockfile 和前一次 plan 对比，确认 replacement 是否真实必要。

### Q52.5 — Senior Trade-off / Edge Case

**问题：** production CI 应如何 gate destroy/replace plan？

**参考答案：**

要求 saved-plan review、policy-as-code 阻止关键 destroy/replace、owner approval、provider version pinning，并让 plan/apply identity 分离。

---

## Q53 — Terraform State Locking

### Q53.1 — 基础定义

**问题：** 为什么 Terraform state 需要 locking？

**参考答案：**

locking 防止两个 writer 同时基于旧 state 做变更并互相覆盖结果。

### Q53.2 — 内部机制

**问题：** 如果两个 applies 同时修改同一个 state，会发生什么？

**参考答案：**

可能产生 duplicate/conflicting resources，且最后 state 只保存其中一个 writer 的视图，破坏 ownership 和 dependency。

### Q53.3 — 生产场景

**问题：** 两个 pipelines 卡在 lock 上，其中一个 lock 看起来 stale。怎么办？

**参考答案：**

先找到 lock holder/run ID，确认 CI/job/process 是否还活跃。不能因为 lock 时间长就直接 force-unlock。

### Q53.4 — 故障排查

**问题：** force-unlock 前如何确认 apply 已不在运行？

**参考答案：**

检查 pipeline status、backend lock metadata、Terraform/cloud activity、operator communication；确认 abandoned 后先备份 state，再用准确 lock ID force-unlock。

### Q53.5 — Senior Trade-off / Edge Case

**问题：** 怎样设计 CI/state ownership，让 force-unlock 几乎不需要？

**参考答案：**

每个 state 串行 apply、使用可靠 remote lock backend、清晰 workspace ownership、支持 clean cancellation，并按 team/blast radius 拆 state。

---

## Q54 — Terraform 中的 Secrets

### Q54.1 — 基础定义

**问题：** `sensitive = true` 能让 Terraform secret 安全吗？

**参考答案：**

`sensitive` 主要隐藏 CLI/UI 展示，不会让 secret 从 state 中消失或自动加密。

### Q54.2 — 内部机制

**问题：** secret 还可能出现在哪里？

**参考答案：**

secret 可能存在 state versions、plan files、CI env、provider debug logs、API payload 和 downstream resource attributes。

### Q54.3 — 生产场景

**问题：** secret 曾经写进 state 后已经 rotation，仍有哪些 exposure？

**参考答案：**

旧 state version、backup、artifact、日志仍可能包含已 rotation 的值。应先 revoke/rotate，再做 exposure assessment。

### Q54.4 — 故障排查

**问题：** 如何保护 remote state、CI logs、plans 和历史版本？

**参考答案：**

使用 encrypted remote state、least-privilege backend access、短期 credentials、sanitized logs、受控 plan artifact，并对历史 state access 做审计。

### Q54.5 — Senior Trade-off / Edge Case

**问题：** 什么时候 Terraform 应只引用 secret manager，而不是直接管理 secret value？

**参考答案：**

更优模式是 Terraform 创建 secret manager/policy/reference，而 runtime 自己取值。只有 provider/resource 需要在 provision 时知道 secret 时才考虑让 Terraform 管值。

---

## Q55 — Terraform Apply 中途失败

### Q55.1 — 基础定义

**问题：** Terraform 创建 15 个资源后在第 16 个失败，state 会怎样？

**参考答案：**

成功完成并被 Terraform 记录的资源通常已经写入 state；失败点之后未执行。下一次 plan 会刷新并计算剩余差异。

### Q55.2 — 内部机制

**问题：** 后续 plan 如何处理 partial progress？

**参考答案：**

provider read 会重新观察 remote state，但如果 side effect 成功而 Terraform 不知道，就可能出现 orphan/duplicate ambiguity。

### Q55.3 — 生产场景

**问题：** 外部 API side effect 已发生，但 provider 尚未成功写 state，为什么麻烦？

**参考答案：**

例如资源已经创建但 ID 没写入 state，下一次 apply 可能再次 create 或报 already exists，因此不能无脑 rerun。

### Q55.4 — 故障排查

**问题：** 如何检查 real infra 与 state 并安全恢复？

**参考答案：**

先备份 state，检查 provider logs 和真实 cloud/API resources，再 refresh/plan；必要时 import orphan resource 或修正 ownership。

### Q55.5 — Senior Trade-off / Edge Case

**问题：** module/provider 如何设计得更适合 partial failure recovery？

**参考答案：**

使用 idempotent provider APIs、deterministic naming、较小 blast radius，并在 module/provider test 中模拟 API timeout、partial create 和 eventual consistency。

---

## Q56 — 大规模 Terraform 管理

### Q56.1 — 基础定义

**问题：** 几百个 production resources、多个 teams 时如何管理 Terraform？

**参考答案：**

使用清晰 state/module ownership、remote backend/locking、CI-only apply、versioned modules、provider lock、policy-as-code、reviewable saved plan 和 drift detection。

### Q56.2 — 内部机制

**问题：** 解释 module boundaries、remote state、CI/CD、policy-as-code、provider pinning 和 ownership。

**参考答案：**

module 是可版本化 source code，不应让所有 consumer 自动漂到 latest。stack/state 边界对应 team 和 blast radius，并通过明确 interface 输出依赖。

### Q56.3 — 生产场景

**问题：** 一个 shared module change 可能影响几十个 stacks。如何降低 blast radius？

**参考答案：**

发布新 module version，在代表性环境验证，先升级 canary stacks，再分批 rollout，并检查每个 consumer 的 plan。

### Q56.4 — 故障排查

**问题：** 如何设计 validation、canary apply、policy check、drift detection 和 recovery？

**参考答案：**

从 fmt/validate/static checks 到 integration test、policy、saved-plan approval、staged apply、post-apply verification 和恢复 runbook，形成完整 pipeline。

### Q56.5 — Senior Trade-off / Edge Case

**问题：** 成熟 platform organization 中哪些应该 centralized，哪些 delegated？

**参考答案：**

平台团队集中安全标准、CI framework、core modules 和 policy；业务 team 自主管理 workload-specific resources。过度集中会成为 bottleneck，完全分散则增加 inconsistency。

---

# 08 — 可观测性

## Q57 — Metrics、Logs 与 Traces

### Q57.1 — 基础定义

**问题：** 什么时候分别使用 metrics、logs 和 traces？

**参考答案：**

metrics 适合趋势、聚合和 alert；logs 适合离散事件和上下文；traces 适合跨服务 request path、latency 和 causality。三者应该组合使用。

### Q57.2 — 内部机制

**问题：** 解释三种 telemetry 在信息模型、成本和用途上的差异。

**参考答案：**

metrics 便宜且适合时间序列；logs 信息丰富但体量大；traces 能看到 span dependency/critical path，但有 sampling 和存储成本。

### Q57.3 — 生产场景

**问题：** 服务 latency 很高但没有明显 errors。你先看哪个 signal，为什么？

**参考答案：**

先用 user-facing latency metrics 确认范围与维度，再用 traces 找慢的 span，最后用 logs/component metrics 验证具体服务或 dependency 的原因。

### Q57.4 — 故障排查

**问题：** 如何把 RED metrics、traces、structured logs 和 infra metrics 串起来定位？

**参考答案：**

边缘使用 RED 定位用户症状，trace 找 slow hop，infra/dependency metrics 验证 saturation，再用结构化 logs 查看具体 error/request context，利用 trace/request ID 串联。

### Q57.5 — Senior Trade-off / Edge Case

**问题：** 哪些 telemetry 你会故意不采集，为什么？

**参考答案：**

不要采集 secrets/PII，不要把 request/user ID 当 metric label，不要无限保留高 volume logs/traces。采集应围绕可回答的 operational questions。

---

## Q58 — RED 与 USE

### Q58.1 — 基础定义

**问题：** 解释 RED 和 USE 方法。

**参考答案：**

RED 是 Rate、Errors、Duration，适合 request-driven services；USE 是 Utilization、Saturation、Errors，适合 CPU、disk、network、pool、queue 等资源。

### Q58.2 — 内部机制

**问题：** 哪些系统更适合 RED，哪些更适合 USE？

**参考答案：**

RED 更接近用户请求体验，USE 更适合回答底层资源是否接近容量或已经出错。两者结合才能从 symptom 走到 cause。

### Q58.3 — 生产场景

**问题：** API 很慢但 CPU 很低。RED 与 USE 会如何指导你？

**参考答案：**

RED 先确认哪些 request 变慢、是否有错误；CPU 低后再用 USE 检查 thread/connection pool、disk、network、queue 或 dependency saturation。

### Q58.4 — 故障排查

**问题：** 如何把 RED/USE 映射到 app、node、network、storage 和 pools？

**参考答案：**

app/edge 看 R/E/D；node CPU、memory pressure、disk queue、NIC drops、DB pool、worker queue 看 U/S/E，并对齐同一时间窗口。

### Q58.5 — Senior Trade-off / Edge Case

**问题：** 如何避免 RED/USE 变成机械 dashboard checklist？

**参考答案：**

先从用户影响和 hypothesis 出发，只保留可行动的指标。框架是思考提示，不是要求把每个 metric 都塞进 dashboard。

---

## Q59 — P99 上升但 P50 稳定

### Q59.1 — 基础定义

**问题：** P99 latency 翻倍但 P50 不变，说明什么？

**参考答案：**

说明典型请求仍正常，但最慢的一小部分显著变差，常见于某些 Pod/node/tenant/route、cache miss、retry 或 dependency path。

### Q59.2 — 内部机制

**问题：** 为什么 tail latency 可以显著变化而 median 不变？

**参考答案：**

P50 只代表中位数，而 P99 对最慢约 1% 极敏感；少量极慢请求不会移动 median，却会明显拉高 tail。

### Q59.3 — 生产场景

**问题：** 只有少量 requests 变慢，应该按哪些 dimensions 分组？

**参考答案：**

按 endpoint、version、AZ/node/Pod、tenant class、request size、cache hit/miss、dependency route 等 bounded dimensions 对比。

### Q59.4 — 故障排查

**问题：** 如何用 traces、endpoint/version/AZ/node、queue 和 dependency timing 找到 tail source？

**参考答案：**

抽取 slow trace exemplars，查看 span critical path、queue/pool waits、GC、retry、node placement 和 dependency timing，并与正常请求对比。

### Q59.5 — Senior Trade-off / Edge Case

**问题：** 怎样做 tail latency alert，既能捕获问题又不被低样本噪声干扰？

**参考答案：**

结合 SLO/traffic volume、足够 histogram sample window 和连续性。低流量 endpoint 可使用更长 window 或固定 latency threshold，避免 percentile 数学噪声。

---

## Q60 — Average Latency 正常但用户很慢

### Q60.1 — 基础定义

**问题：** 为什么 average latency 正常，但用户仍可能觉得系统很慢？

**参考答案：**

average 会被大量快速请求稀释，无法显示 tail 或局部 cohort 问题。应同时看 percentiles 和分群指标。

### Q60.2 — 内部机制

**问题：** 解释 skew、outlier、percentile、aggregation window 和 request weighting。

**参考答案：**

分布若高度 skew，mean 代表性很差；aggregation window 太长也会把短时事故抹平；高流量 endpoint 会支配全局平均。

### Q60.3 — 生产场景

**问题：** 一个 customer cohort latency 3 秒，而 global average 150ms。怎么暴露这个问题？

**参考答案：**

使用有界维度如 region、plan tier、endpoint、version、shard 查看 percentiles/error，并通过 logs/traces 定位具体 customer。

### Q60.4 — 故障排查

**问题：** 如何既按 tenant/region/endpoint/version 切片，又控制 cardinality？

**参考答案：**

metric labels 只放 bounded dimensions；customer/request ID 放日志或 trace，并用 exemplar/trace ID 从 metric pivot 到高 cardinality detail。

### Q60.5 — Senior Trade-off / Edge Case

**问题：** 如何同时给 leadership 简单 KPI 和 operations 足够细节？

**参考答案：**

领导层可以看 global SLO、P95/P99 和 worst-cohort health；运维保留 segmented dashboards。不能让单一 global average 成为唯一健康标准。

---

## Q61 — High-cardinality Metrics

### Q61.1 — 基础定义

**问题：** 什么是 metric cardinality？

**参考答案：**

cardinality 是 metric 所有 label 组合形成的 unique time series 数量。label value 越不受控，series 会指数增长。

### Q61.2 — 内部机制

**问题：** 为什么 user ID、request ID 等 labels 很危险？

**参考答案：**

user/request ID 几乎每次都不同，与其他 labels 组合后会产生海量 series，消耗 memory、index、storage 和 query resources。

### Q61.3 — 生产场景

**问题：** 团队给所有 Prometheus metrics 加了 `customer_id` 后 monitoring 崩了。发生了什么？

**参考答案：**

`customer_id` 使每个 customer × endpoint × status × instance 等组合都变成独立 series，TSDB 可能被 series churn 和内存占用压垮。

### Q61.4 — 故障排查

**问题：** 如何快速识别和缓解 cardinality explosion，同时保留诊断价值？

**参考答案：**

找 top cardinality metrics/labels，立即 drop/relabel 或 rollback offending change，把 per-customer detail 转移到 logs/traces，保留 service-level aggregated metrics。

### Q61.5 — Senior Trade-off / Edge Case

**问题：** 你会制定哪些 label、exemplar、aggregation 和 retention 规则？

**参考答案：**

建立 label allowlist、bounded value 规则、cardinality budget、review 和 automated checks；unique IDs 使用 logs/traces/exemplars，不进入主 metric labels。

---

## Q62 — 故障时没有有用 Logs

### Q62.1 — 基础定义

**问题：** production 正在失败，但 application logs 看不出异常。下一步怎么办？

**参考答案：**

先确认 failure 在哪一层发生。请求可能根本没到 application，因此 app logs 正常并不奇怪，应检查 DNS、LB/proxy、network、auth、runtime 和 dependencies。

### Q62.2 — 内部机制

**问题：** 为什么“没有 logs”不等于“没有 failure”？

**参考答案：**

只有执行到被 instrumented code path 才会产生日志。kernel drop、process kill、proxy rejection、sampling 或缺失 instrumentation 都可能造成无日志故障。

### Q62.3 — 生产场景

**问题：** proxy 在请求到达 app 前就生成 5xx，这会如何改变诊断？

**参考答案：**

将重点转到 proxy/LB access/error logs、target health、routing/network，而不是继续搜索 application logs。

### Q62.4 — 故障排查

**问题：** 如何利用 traces、LB/proxy logs、kernel/network telemetry、dependency metrics 和 synthetic tests？

**参考答案：**

用 edge metrics/logs、trace 中缺失的 spans、system/network telemetry、synthetic probe 和 dependency data，比较成功与失败请求的第一个差异。

### Q62.5 — Senior Trade-off / Edge Case

**问题：** 应该建立怎样的 logging contract？

**参考答案：**

记录关键 lifecycle、decision、error 和 correlation IDs，保证结构化且不过量；同时用 metrics 监控 expected event 缺失，而不是依赖日志数量本身。

---

## Q63 — Distributed Tracing

### Q63.1 — 基础定义

**问题：** distributed tracing 如何帮助诊断 microservices？

**参考答案：**

distributed trace 把一次逻辑请求跨服务的 spans 关联起来，显示调用层级、时间、错误、retry 和 dependency path。

### Q63.2 — 内部机制

**问题：** 解释 trace ID、span、parent/child、context propagation、sampling 和 baggage。

**参考答案：**

trace ID 关联整条请求；span 表示一段工作；parent-child 表示因果/层级；context propagation 让下游继续同一 trace；sampling 控制采集量；baggage 传播少量上下文。

### Q63.3 — 生产场景

**问题：** 一个 request 经过 8 个 services，共耗时 4 秒。如何找主要 contributor？

**参考答案：**

查看 critical path 和最长 spans，区分 self time 与 child time，找出真正贡献 4 秒的 service/dependency/queue。

### Q63.4 — 故障排查

**问题：** 如何区分 service time、queue time、retry、network delay 和 missing spans？

**参考答案：**

查看 repeated spans、retry、span 之间的空白、queue producer/consumer timing、network/connect attributes，并与 logs/metrics 对齐。

### Q63.5 — Senior Trade-off / Edge Case

**问题：** 怎样的 sampling strategy 能控制成本又保留 incident value？

**参考答案：**

可结合低比例 head sampling 与 error/latency-tail sampling（平台支持时），优先保留 rare failures 和 slow traces，避免全部采集造成成本失控。

---

## Q64 — Alert 设计

### Q64.1 — 基础定义

**问题：** CPU 95% 持续 10 分钟，应该 page SRE 吗？

**参考答案：**

不一定。CPU 是 resource condition，不是用户影响本身。若没有 saturation/throttling 或 SLO 影响，可能只需要观察或容量 ticket。

### Q64.2 — 内部机制

**问题：** symptom alert 与 cause/resource alert 有什么区别？

**参考答案：**

symptom alert 直接反映用户体验，如 errors/latency/SLO burn；cause alert 反映潜在资源原因。cause alert 只有在能预测紧急 failure 且有动作时才适合 page。

### Q64.3 — 生产场景

**问题：** CPU 95%，但 latency/error 正常。应该做什么？

**参考答案：**

检查 headroom、throttling、趋势和 autoscaling；若能提前 scale 可处理，但不应仅因静态阈值凌晨叫醒人。

### Q64.4 — 故障排查

**问题：** 如何结合 SLO burn、saturation、queue depth 和 user impact 决定 paging 或 ticket？

**参考答案：**

当 SLO burn/latency/error 显著、queue/pool saturation 接近不可恢复且需要人工立即动作时 page；慢性容量问题进入 ticket/planning。

### Q64.5 — Senior Trade-off / Edge Case

**问题：** 一个高质量 on-call alert 应具备什么特征？

**参考答案：**

明确 ownership、urgency、customer consequence、上下文和 first action；如果凌晨收到后没人知道该做什么，这个 alert 很可能不该 page。

---

# 09 — SLI / SLO / Reliability

## Q65 — SLI

### Q65.1 — 基础定义

**问题：** 什么是 SLI？

**参考答案：**

SLI 是对用户相关服务行为的量化指标，例如成功率、latency threshold 达标率、freshness、correctness 或 durability。

### Q65.2 — 内部机制

**问题：** 为什么 SLI 应接近 user-visible behaviour，而不是内部 component health？

**参考答案：**

CPU、Pod readiness 等主要是诊断指标；用户可能在这些指标健康时仍失败，因此 availability SLI 应尽量在用户边界测量。

### Q65.3 — 生产场景

**问题：** payment API 可以选择哪些 SLI？哪些不合适？

**参考答案：**

可考虑有效 payment intents 的正确成功率、end-to-end latency、duplicate/correctness、processing freshness。明显 invalid client requests 通常不应计为服务失败。

### Q65.4 — 故障排查

**问题：** 如何验证 SLI query 正确处理 retries、partial failures、client errors 和 missing telemetry？

**参考答案：**

用 raw events 和已知 incidents 回放验证 query，检查 retry/duplicate、async completion、client cancel、status mapping 和 telemetry gaps 是否导致误算。

### Q65.5 — Senior Trade-off / Edge Case

**问题：** 什么样的 SLI 才适合长期治理？

**参考答案：**

业务意义稳定、定义有版本管理、可解释，并在产品 flow 变化时通过正式 migration 更新，而不是悄悄改 query。

---

## Q66 — SLO 与 SLA

### Q66.1 — 基础定义

**问题：** SLO 和 SLA 的区别是什么？

**参考答案：**

SLO 是内部针对某 SLI 的 reliability objective；SLA 是对外合同/业务承诺，可能伴随赔偿或条款。SLO 通常应比 SLA 更严格。

### Q66.2 — 内部机制

**问题：** 解释 internal reliability target、contractual commitment 和 error budget。

**参考答案：**

SLO 留出 operating margin，并通过 error budget 指导 delivery/reliability trade-off；SLA breach 是外部 contractual event，不应成为日常目标线。

### Q66.3 — 生产场景

**问题：** service SLO 99.9%，SLA 99.5%，为什么合理？

**参考答案：**

99.9% internal target 为 99.5% contractual commitment 提供 buffer，让团队在真正 SLA breach 前就采取 reliability action。

### Q66.4 — 故障排查

**问题：** 内部 telemetry 显示 SLO 达标，但 customers 声称 SLA impact。怎么办？

**参考答案：**

调查 measurement coverage 和 SLA 定义，确认是否漏了某 cohort、region、endpoint、correctness 或 data freshness。不能因为 internal dashboard green 就否认客户体验。

### Q66.5 — Senior Trade-off / Edge Case

**问题：** SLO 除了报一个百分比，还应该推动哪些组织行为？

**参考答案：**

SLO 应影响 release pace、reliability investment、incident prioritization 和 roadmap。一个从不改变工程决策的 SLO 价值有限。

---

## Q67 — 为什么选择 99.9% 而非 99.99%

### Q67.1 — 基础定义

**问题：** 为什么团队可能选择 99.9% 而不是 99.99%？

**参考答案：**

SLO 应来自用户/业务需求和可承担成本。99.99% 的 error budget 只有 99.9% 的约十分之一，通常需要更强 redundancy、change control 和 operations。

### Q67.2 — 内部机制

**问题：** 把这两个 target 转换成 engineering cost 和 allowable failure。

**参考答案：**

每增加一个 nine 往往非线性增加成本：更多 capacity、multi-region/failover、更谨慎 deploy、更高 dependency requirement 和 on-call maturity。

### Q67.3 — 生产场景

**问题：** 一个低价值 internal service 要求 four nines，因为“可靠性很重要”。你如何挑战？

**参考答案：**

询问 99.9% 时真实用户损失是什么、four nines 带来多少业务价值，以及依赖是否能达到。不能用数字彰显“高级”。

### Q67.4 — 故障排查

**问题：** 如何判断 architecture、staffing、dependencies 和 operational maturity 能否支撑目标？

**参考答案：**

分析历史 availability/incident、dependency SLO、maintenance、architecture、staffing 和 failover maturity。如果结构上做不到，高目标只会产生错误激励。

### Q67.5 — Senior Trade-off / Edge Case

**问题：** 什么 business evidence 才值得提高 SLO？什么时候应刻意保持较低目标？

**参考答案：**

当 revenue、regulatory、user harm 或竞争需求明确证明额外可靠性价值时提高；如果额外成本高于收益，就应保持现实目标。

---

## Q68 — Error Budget

### Q68.1 — 基础定义

**问题：** 什么是 error budget？

**参考答案：**

error budget 是 SLO 允许的 unreliability。例如 99.9% SLO 对应约 0.1% 可失败事件/时间窗口。

### Q68.2 — 内部机制

**问题：** 它如何连接 SLO、unreliability 和 delivery velocity？

**参考答案：**

预算健康时可以正常迭代；快速 burn 表示当前 reliability risk 过高，需要降低 risky change 或投入修复。

### Q68.3 — 生产场景

**问题：** 一个月预算前两天就烧掉 80%，怎么办？

**参考答案：**

先稳定系统，找出 burn 来源，暂停高风险 release，并优先处理导致快速消耗的 incident/systemic issue。

### Q68.4 — 故障排查

**问题：** 如何区分 one-off incident 与结构性 reliability problem，并决定 release policy？

**参考答案：**

判断 burn 是否来自已经解决的一次事故，还是持续 defect/recurrence。结合 burn trend、change failure、known risks 决定 freeze、部分限制或 targeted fixes。

### Q68.5 — Senior Trade-off / Edge Case

**问题：** 怎样的 error-budget policy 不会变成僵硬的 release freeze？

**参考答案：**

把 policy 当 decision framework，不是惩罚机制；允许明确风险 owner 的例外，并在指标恢复后及时恢复 delivery。

---

## Q69 — Payment API Availability SLI

### Q69.1 — 基础定义

**问题：** 如何为 payment API 定义 availability？

**参考答案：**

围绕有效 payment intent 是否得到正确、及时、无歧义的结果定义，而不是简单统计 HTTP 200。

### Q69.2 — 内部机制

**问题：** numerator/denominator 应如何选择？

**参考答案：**

denominator 应是合法可处理 requests；numerator 是满足 correctness/latency contract 的成功结果。malformed client request 通常不算服务 failure。

### Q69.3 — 生产场景

**问题：** client timeout，但 server 后来成功完成 payment，这算 available 吗？

**参考答案：**

用户看到 timeout 且不知道是否扣款，是一个 ambiguous outcome。即使后端最终成功，从用户体验和安全性看通常应视为 availability/correctness 问题。

### Q69.4 — 故障排查

**问题：** 如何处理 idempotency、async processing、retry、partial success 和 duplicate submission？

**参考答案：**

使用 idempotency key 和 durable transaction state 区分 accepted/committed/duplicate/failed/unknown，并避免 retry 导致重复扣款。

### Q69.5 — Senior Trade-off / Edge Case

**问题：** transactional system 最终应该用什么 user outcome 定义 availability？

**参考答案：**

核心是用户能否安全完成付款且不会重复收费或陷入不确定状态，所以 correctness 与 unambiguous outcome 和 reachability 同样重要。

---

## Q70 — Dependency SLO 不匹配

### Q70.1 — 基础定义

**问题：** 你的服务 SLO 99.95%，但必须依赖一个 99.9% 服务。有什么问题？

**参考答案：**

如果依赖是 mandatory 且无法屏蔽其 failure，你几乎不可能长期承诺比它更高的 end-to-end availability。

### Q70.2 — 内部机制

**问题：** dependency reliability 如何组合？

**参考答案：**

串行 mandatory dependencies 会消耗整体 availability/error budget。弱依赖的 0.1% failure 本身就可能超过你 99.95% 所允许的预算。

### Q70.3 — 生产场景

**问题：** dependency 无法替换，有哪些 architectural options？

**参考答案：**

可使用 cache/stale data、alternative provider、async queue、local fallback、reduced functionality、bulkhead，或者重新谈自己的 SLO。

### Q70.4 — 故障排查

**问题：** 如何测试 cache、graceful degradation、retry、hedging、async decoupling 是否真的保护 SLO？

**参考答案：**

通过 dependency failure injection/game day 验证 fallback 是否维持真正 user SLI，同时检查 cache age、queue backlog、recovery surge、data correctness 和 retry load。

### Q70.5 — Senior Trade-off / Edge Case

**问题：** 什么时候应下调自己的 SLO，而不是用复杂度掩盖弱 dependency？

**参考答案：**

如果业务在 dependency down 时天然无法工作，而且额外 resilience 复杂度收益低，就应诚实调整 SLO，而不是制造一个数学上漂亮但不可实现的目标。

---

## Q71 — SLO Violation Alerting

### Q71.1 — 基础定义

**问题：** 为什么不能每次 SLO violation 都 page？

**参考答案：**

SLO 是窗口目标，短暂 spike 可能只消耗很少 budget。每个瞬时 violation 都 page 会造成 alert fatigue。

### Q71.2 — 内部机制

**问题：** 解释 short-window noise、long-window significance 和 burn rate。

**参考答案：**

burn rate 表示预算消耗速度。短窗口适合捕获 catastrophic fast burn，长窗口用于确认持续性和慢性问题。

### Q71.3 — 生产场景

**问题：** 服务 5 分钟短暂超过 error target，但月度 budget 很充足。需要 page 吗？

**参考答案：**

如果已恢复、预算充足且不会快速耗尽，通常不需要 page，可记录并后续分析。若 user impact/security/correctness 严重则另当别论。

### Q71.4 — 故障排查

**问题：** 如何设计 multi-window burn-rate alerts？

**参考答案：**

使用短+长窗口确认 fast burn，再用更长窗口组合检测 slow burn，阈值应和预计预算耗尽时间关联。

### Q71.5 — Senior Trade-off / Edge Case

**问题：** 还应该补充哪些 secondary signals？

**参考答案：**

加入 traffic validity、重大 correctness/security event、hard capacity risk 和 telemetry gap。SLO alert 很强，但不能表达所有 urgent incidents。

---

## Q72 — Burn-rate Alerting

### Q72.1 — 基础定义

**问题：** 什么是 SLO burn rate？

**参考答案：**

burn rate = 当前 error-budget 消耗速度 ÷ 可持续消耗速度。`1x` 意味着按当前速度正好在整个 SLO window 用完预算。

### Q72.2 — 内部机制

**问题：** `1x`、`10x`、`100x` 分别意味着什么？

**参考答案：**

`10x` 表示消耗速度是允许值 10 倍，`100x` 更快，因此可推算预算会在多短时间内耗尽。

### Q72.3 — 生产场景

**问题：** 如何理解快速 50x burn 与持续 2x burn？

**参考答案：**

50x 是 acute incident，需要快速 page；2x 如果持续很久也会耗尽预算，更适合较长 window 的慢 burn alert/ticket。

### Q72.4 — 故障排查

**问题：** 设计一种能同时发现 catastrophic 和 slow burn 的 multi-window 策略。

**参考答案：**

设置 fast-burn short/long 窗口 pair，以及 slow-burn 更长 pair；要求两个窗口同时满足阈值以降低噪声，并用历史 incidents 调参。

### Q72.5 — Senior Trade-off / Edge Case

**问题：** 在数学精度、运维简洁和 on-call 可解释性之间如何取舍？

**参考答案：**

宁可少量清晰、团队都理解的 burn alerts，也不要建立没人会解释的复杂公式体系。可行动性优先于理论上的完美精度。

---

# 10 — 编程 / 并发

## Q73 — Deadlock

### Q73.1 — 基础定义

**问题：** 什么是 deadlock？画一个简单例子。

**参考答案：**

deadlock 是多个执行者互相等待对方持有的资源而无法继续。例如 Thread A 持有 X 等 Y，Thread B 持有 Y 等 X。

### Q73.2 — 内部机制

**问题：** 解释 resource dependency cycle 为什么让 threads 永久 blocked。

**参考答案：**

等待图形成 cycle，任何一方都到不了 release resource 的代码，所以 progress 永久停止。

### Q73.3 — 生产场景

**问题：** Java service CPU 很低但完全没 progress。什么 evidence 会让 deadlock 成为主要 hypothesis？

**参考答案：**

请求吞吐突然归零/大降、CPU 很低、blocked thread 数上升，并且 thread dumps 显示 reciprocal lock ownership，是强证据。

### Q73.4 — 故障排查

**问题：** 如何用 thread dumps、lock ownership、JVM tools 和 telemetry 确认？

**参考答案：**

连续抓几份 thread dump，看 `BLOCKED`/waiting locks 和 owners，结合 JVM profiler/lock data 与代码路径。重启前尽量保存 dumps。

### Q73.5 — Senior Trade-off / Edge Case

**问题：** 哪些设计 pattern 可以防止 deadlock？有什么 trade-off？

**参考答案：**

使用固定 lock ordering、减少 nested locks、缩小 critical sections、timeout、immutable/message-passing 或 lock-free structures。timeout 只能避免永久等待，不自动保证数据正确。

---

## Q74 — Deadlock 四个条件

### Q74.1 — 基础定义

**问题：** deadlock 需要哪四个条件？

**参考答案：**

Coffman conditions：mutual exclusion、hold and wait、no preemption、circular wait。经典 deadlock 需要四者同时成立。

### Q74.2 — 内部机制

**问题：** 解释 mutual exclusion、hold-and-wait、no preemption、circular wait。

**参考答案：**

资源独占、持有资源同时等待其他资源、资源不能被强制夺走、等待关系形成环，这四者共同造成无法前进。

### Q74.3 — 生产场景

**问题：** 应用代码中最现实地打破哪个条件？

**参考答案：**

实践中常通过统一 lock acquisition order 打破 circular wait；更好的设计是减少 nested shared locks。

### Q74.4 — 故障排查

**问题：** 如何在事故前发现 locking design 可能形成 cycle？

**参考答案：**

建立 lock-order graph、审查 nested critical sections、使用 static/runtime concurrency diagnostics，并通过 stress tests 覆盖高并发 path。

### Q74.5 — Senior Trade-off / Edge Case

**问题：** 大型 codebase 中如何记录和 enforce concurrency invariants？

**参考答案：**

把 lock hierarchy 和 ownership 封装在少数 API 中，通过 code review/testing enforce；更优方案是减少 shared mutable state，让 invariant 更局部。

---

## Q75 — Race Condition 与 Deadlock

### Q75.1 — 基础定义

**问题：** race condition 和 deadlock 有什么区别？

**参考答案：**

race condition 是执行 interleaving 导致错误结果；deadlock 是等待 cycle 导致无法继续。一个影响 correctness，一个主要影响 progress。

### Q75.2 — 内部机制

**问题：** timing 在 race 中如何影响 correctness，而 deadlock 如何影响 progress？

**参考答案：**

race 可能继续运行但 state 错了；deadlock 则相关 threads/work 停住。两者都来自 concurrency，但检测和修复方法不同。

### Q75.3 — 生产场景

**问题：** counter 偶尔丢 update，但服务从不卡死。更像什么问题？

**参考答案：**

典型是非原子的 read-modify-write race，例如两个线程同时读取旧 counter 并覆盖彼此结果。

### Q75.4 — 故障排查

**问题：** 如何复现和证明每百万请求才出现一次的 race？

**参考答案：**

做高并发 stress test、记录 invariant/sequence、降低 nondeterminism、使用 thread sanitizer（支持时）或 instrumentation，把问题缩到最小 shared state。

### Q75.5 — Senior Trade-off / Edge Case

**问题：** 什么时候选择 atomics、locks、immutability、actor model 或 database constraints？

**参考答案：**

简单 independent state 用 atomics；compound invariant 用 locks；能 immutable/ownership 更好；跨 process persistent invariant 优先让 database transaction/constraint 保证。

---

## Q76 — Thread Safety

### Q76.1 — 基础定义

**问题：** 什么让代码成为 thread-safe？

**参考答案：**

thread-safe 代码在合法并发 interleaving 下仍保持 invariant，通常依赖 immutability、confinement、atomic operations、synchronization 或 concurrency-safe abstractions。

### Q76.2 — 内部机制

**问题：** 解释 atomicity、visibility、immutability 和 shared mutable state。

**参考答案：**

正确性不仅是 atomicity，还包括 visibility/order；语言 memory model 决定一个 thread 的 write 何时能被其他 thread 看见。

### Q76.3 — 生产场景

**问题：** singleton cache 在测试正常，但生产并发下数据损坏。检查什么？

**参考答案：**

检查非线程安全 collections、check-then-act、unsafe publication、mutable cache entries、double-checked locking 或不足的 synchronization。

### Q76.4 — 故障排查

**问题：** 如何通过 stress tests、thread analyzers 和 memory-model reasoning 找 bug？

**参考答案：**

构建并发 stress test 和 invariant assertions，结合 thread/concurrency tooling、调用栈和最小复现，不要先随便加一个全局锁。

### Q76.5 — Senior Trade-off / Edge Case

**问题：** 哪些设计选择能减少显式 locking？

**参考答案：**

优先 immutable values、thread confinement、concurrent collections、message passing 和 ownership。locks 用于真正 compound invariants，避免巨大的共享 critical section。

---

## Q77 — Database Concurrency / Lost Update

### Q77.1 — 基础定义

**问题：** 两个 writers 如何导致 lost update？

**参考答案：**

两个 writer 读取同一个旧值，各自计算后写回，后写入者覆盖前一个结果，而系统没有检测冲突，这就是 lost update。

### Q77.2 — 内部机制

**问题：** 解释 optimistic lock、pessimistic lock、MVCC、compare-and-set 和 version column。

**参考答案：**

optimistic locking 使用 version/conditional update；pessimistic locking 提前锁住 row；MVCC 提供 snapshot；CAS/unique constraint 可原子验证条件。

### Q77.3 — 生产场景

**问题：** 两个 service instances 同时更新一个 order。如何保证 correctness？

**参考答案：**

冲突少时可用 version column + conditional update/retry；需要严格串行 invariant 时可用 transaction/row lock；所有 retry 还要 idempotent。

### Q77.4 — 故障排查

**问题：** 如何复现 race、选择控制机制并验证 fix？

**参考答案：**

并发发起两个 transactions，记录 isolation/SQL，加入 version/constraint 后再次 stress test，并验证 conflict/retry path 不会产生重复 side effect。

### Q77.5 — Senior Trade-off / Edge Case

**问题：** 什么时候 application-level lock 不如 database invariant？

**参考答案：**

数据库能看到所有 writers，并能用 transaction/constraint 原子 enforce persistent invariant；仅应用内锁在多实例环境很容易失效。

---

## Q78 — Distributed Lock

### Q78.1 — 基础定义

**问题：** 什么时候需要 distributed lock？

**参考答案：**

只有多个独立 processes 必须对同一资源串行，而数据库 CAS/constraint、queue ownership 等更简单原语无法满足时才考虑 distributed lock。

### Q78.2 — 内部机制

**问题：** 解释 lease expiry、fencing token、partition、clock 和 stale holder 风险。

**参考答案：**

lease 可能在 holder pause/partition 时过期，新 holder 获锁后旧 holder 仍可能恢复。仅有“我认为我持锁”并不能阻止 stale write。

### Q78.3 — 生产场景

**问题：** lock holder pause 超过 lease，恢复后继续写旧数据。发生了什么？

**参考答案：**

旧 holder 的 lease 已失效，但它不知道或无法被强制停止，因此可能和新 holder 同时操作受保护资源。

### Q78.4 — 故障排查

**问题：** 如何让 expired holder 无法破坏数据？

**参考答案：**

使用单调递增 fencing token，由真正的数据资源在写入时校验 token，拒绝 stale holder；同时测试 pause、partition、expiry 场景。

### Q78.5 — Senior Trade-off / Edge Case

**问题：** 什么时候应该 redesign 以完全避免 distributed lock？

**参考答案：**

如果可用 queue partition ownership、DB transaction/unique constraint、CAS、single-writer architecture，应优先这些，因为 distributed lock 增加一个复杂 coordination dependency。

---

## Q79 — Idempotency

### Q79.1 — 基础定义

**问题：** 什么是 idempotency？为什么 distributed systems 需要它？

**参考答案：**

idempotent operation 允许同一个逻辑请求重复执行，而不会重复产生业务 side effect。网络 timeout 让 caller 无法知道第一次是否成功，因此 retry 必须安全。

### Q79.2 — 内部机制

**问题：** 解释 idempotency key、dedup state、replay semantics 和 response persistence。

**参考答案：**

客户端提供稳定 key，服务原子保留 key 并记录 request fingerprint、status 和最终 response，后续相同 key 返回原结果。

### Q79.3 — 生产场景

**问题：** 设计一个可能因 timeout 被 retry 的 idempotent payment API。

**参考答案：**

在收费前持久化 idempotency key/transaction state，重复 key 必须验证 payload 相同；若首次 response 丢失，重试只查询/返回原 payment outcome，不再次扣款。

### Q79.4 — 故障排查

**问题：** 如何处理 concurrent duplicates、partial downstream success 和 key expiry？

**参考答案：**

用 unique constraint/transaction 处理并发 duplicate，明确 `IN_PROGRESS/SUCCEEDED/FAILED/UNKNOWN`，对下游 ambiguous result 做 reconciliation，key retention 覆盖合理 retry window。

### Q79.5 — Senior Trade-off / Edge Case

**问题：** 跨多个独立系统时，现实中能提供什么级别的 guarantee？

**参考答案：**

真正跨多系统 exactly-once 很难；更现实的是 at-least-once requests/messages + 每个 side-effect boundary 的 durable idempotency + reconciliation。

---

## Q80 — Retry Storm

### Q80.1 — 基础定义

**问题：** dependency 变慢，所有 clients 都 retry 3 次，会发生什么？

**参考答案：**

依赖容量下降的同时请求反而倍增，会导致 queue、thread/connection pool、CPU 等进一步 saturation，造成更慢、更 timeout、更多 retry 的正反馈。

### Q80.2 — 内部机制

**问题：** 解释 retry amplification、queue growth、timeout、pool exhaustion 和 cascading failure。

**参考答案：**

每次 retry 和 original call 同样消耗资源；如果 SDK、service mesh、app、caller 都 retry，一次用户请求可能放大成很多 backend attempts。

### Q80.3 — 生产场景

**问题：** 下游从 50ms 变成 2s，上游 QPS 因 retries 变成三倍。立即怎么做？

**参考答案：**

立即降低 retry pressure：限制/关闭非必要 retry、circuit break、load shed、保护 queue/pools，必要时减少上游流量。先稳定再优化。

### Q80.4 — 故障排查

**问题：** 如何配置 timeout、backoff、jitter、retry budget、circuit breaker 和 load shedding？

**参考答案：**

只 retry 明确 transient 且 safe 的操作，使用 bounded attempts、exponential backoff + jitter、end-to-end deadline、retry budget，并设置 bulkhead/circuit breaker。

### Q80.5 — Senior Trade-off / Edge Case

**问题：** 多层架构中 retry 应放在哪里，避免层层相乘？

**参考答案：**

retry 应放在最了解语义、idempotency 和 remaining deadline 的层。避免多个独立层同时 retry，同一次 failure 被乘法放大。

---

# 11 — 软件工程基础

## Q81 — SOLID

### Q81.1 — 基础定义

**问题：** 你最认可哪个 SOLID principle？为什么？

**参考答案：**

选择一个你真正有经验的原则并说明工程价值。例如 Dependency Inversion 能让 business logic 不直接依赖 DB/cloud SDK，从而更易测试、更换实现和控制变更风险。

### Q81.2 — 内部机制

**问题：** 不要背定义，解释它如何影响 dependency structure。

**参考答案：**

重点讲 dependency direction、boundary、contract 和 coupling，而不是只解释首字母。

### Q81.3 — 生产场景

**问题：** 举一个违反该原则导致 production change/testing 困难的真实例子。

**参考答案：**

例如 service 的核心业务逻辑直接调用具体 AWS SDK/DB，使 unit test、local development 和 provider migration 都困难，事故中也难隔离 dependency。

### Q81.4 — 故障排查

**问题：** 如何在不引入大规模 rewrite 的情况下安全 refactor？

**参考答案：**

先加 characterization/regression tests，再引入 adapter/interface seam，把依赖逐步移到边界，每次小步 deploy 验证，而不是一次性重写。

### Q81.5 — Senior Trade-off / Edge Case

**问题：** 什么时候严格套用 SOLID 反而会让系统更糟？

**参考答案：**

过度 SOLID 会产生大量无意义 interfaces、tiny classes 和 indirection，阅读成本高于收益。原则应解决真实 change/risk，而不是作为宗教。

---

## Q82 — Maintainable Code

### Q82.1 — 基础定义

**问题：** 什么是 maintainable code？

**参考答案：**

可维护代码应容易理解、修改、测试、运行和删除。它有清晰边界、低不必要耦合、可读 naming、可靠 tests、足够 observability 和简单 control flow。

### Q82.2 — 内部机制

**问题：** 从 cohesion、coupling、naming、tests、boundaries、observability 和 simplicity 解释。

**参考答案：**

maintainability 本质是工程经济性：一个团队能否低风险、低成本地持续 change。架构、deployment、docs 和 runtime diagnosability 都有关。

### Q82.3 — 生产场景

**问题：** 接手一个稳定但极难修改的服务，如何判断 maintainability 是否真的伤害业务？

**参考答案：**

看 lead time、change-failure rate、重复 regression、incident diagnosis time、onboarding time、重复修同一类 bug，而不是只看“代码难看”。

### Q82.4 — 故障排查

**问题：** 如何 incremental refactor 而不变成 risky rewrite？

**参考答案：**

先找最痛的 seam，补 characterization tests，逐步抽 boundary/adapter，拆小 change 并持续 deploy。不要为了“clean”冻结三个月重写。

### Q82.5 — Senior Trade-off / Edge Case

**问题：** 如何平衡 maintainability、performance、delivery speed 和团队 skill level？

**参考答案：**

真正需要性能的热点可以局部复杂；技能较弱团队更需要直接可读设计。有时少量 duplication 比高度抽象 framework 更可维护。

---

## Q83 — Test-first 与 Test-second

### Q83.1 — 基础定义

**问题：** test-first 和 test-second 各有什么优缺点？

**参考答案：**

test-first 提前反馈 design/testability，test-second 对 exploration 更自然。两者都能写出好或坏测试，关键是测试是否真实提高 confidence。

### Q83.2 — 内部机制

**问题：** 它们如何影响 design feedback、coverage、coupling 和 developer flow？

**参考答案：**

test-first 容易暴露 coupling，但也可能为了 test 过度设计；test-second 可能被跳过或漏边界，但实现明确后更容易针对真实 behaviour 写测试。

### Q83.3 — 生产场景

**问题：** 生产 incident fix 时你会选哪种？为什么？

**参考答案：**

事故 fix 若可行，先写能复现 failure 的最小 regression test，再修；若系统耦合严重，先用 integration/characterization test 锁住 bug 更现实。

### Q83.4 — 故障排查

**问题：** legacy code 很难 isolate，如何加入 regression test？

**参考答案：**

在可控制环境搭 DB/container/fake 或 end-to-end harness，先捕获 failure，再逐步把核心逻辑抽出来补 unit tests。

### Q83.5 — Senior Trade-off / Edge Case

**问题：** 什么时候坚持严格 TDD 会变得 counterproductive？

**参考答案：**

prototype、incident mitigation、UI/exploration、integration-heavy 场景可能不适合严格 TDD。目标是快速可靠 feedback，而不是遵守某个流程形式。

---

## Q84 — Unit Test 与 Integration Test

### Q84.1 — 基础定义

**问题：** 哪些内容适合 unit test，哪些适合 integration test？

**参考答案：**

纯逻辑和边界条件适合快速 deterministic unit tests；DB、serialization、network contract、config、auth 等真实 integration behaviour 需要 integration/contract tests。

### Q84.2 — 内部机制

**问题：** 解释 confidence、speed、dependency realism 和 failure localization 的 trade-off。

**参考答案：**

unit 快且定位清晰但可能过度 mock；integration 更真实但慢且 failure 原因更多；end-to-end confidence 高但成本更高。

### Q84.3 — 生产场景

**问题：** 几千 unit tests 全绿，但 production 总因 config/dependency 出问题。缺什么？

**参考答案：**

缺少真实 wiring/config/schema/dependency 的 tests。应从 incident history 找最常失败的 integration boundaries 补 coverage。

### Q84.4 — 故障排查

**问题：** 如何重构 test pyramid/test portfolio 来捕获真实故障？

**参考答案：**

加入 DB migration、API contract、config loading、auth、queue、container startup、deployment smoke 等测试，同时保留核心 domain logic 的 unit tests。

### Q84.5 — Senior Trade-off / Edge Case

**问题：** 每 commit、pre-merge、pre-deploy、post-deploy 分别跑什么？

**参考答案：**

commit 跑 unit/static；pre-merge 跑关键 integration/contract；pre-deploy 做更广环境验证；post-deploy 做 smoke/synthetic/canary verification。

---

## Q85 — Mocking

### Q85.1 — 基础定义

**问题：** 什么时候 mocking 有用？

**参考答案：**

当 dependency 很慢、不稳定、昂贵，或者该 dependency 本身不在本次 test scope 内时，mocking 很有价值。它的目的应是隔离，而不是伪造真实世界。

### Q85.2 — 内部机制

**问题：** mock、stub、fake 有什么区别？

**参考答案：**

stub 主要返回预设值；mock 通常还验证 interaction；fake 是可运行的轻量实现。过度 mock 内部 class 会让 test 和 implementation 强耦合。

### Q85.3 — 生产场景

**问题：** test suite 有大量脆弱 mocks，全部通过，但 production contract 已经 drift。问题在哪里？

**参考答案：**

tests 验证的是自己虚构的 behaviour，而不是 dependency 的真实 contract。production contract 变化后 mocks 没跟着变，所以测试给了虚假 confidence。

### Q85.4 — 故障排查

**问题：** 如何用 contract test、fake 或真实 integration 替换高风险 mocks，同时避免 CI 过慢？

**参考答案：**

优先替换关键外部 boundary：引入 consumer/provider contract tests、test containers、可复用 fake 和小规模真实 integration suite。把慢测试分层，而不是全塞到每次 commit。

### Q85.5 — Senior Trade-off / Edge Case

**问题：** 哪些 boundary 适合 mock，哪些更应该 against real implementation 测试？

**参考答案：**

纯内部、昂贵且行为简单的 boundary 可 mock；database semantics、serialization、HTTP contract、auth、message broker 等容易出现真实差异的地方应保留 integration/contract coverage。

---

## Q86 — API Versioning

### Q86.1 — 基础定义

**问题：** 如何修改被广泛使用的 API 而不破坏现有 consumers？

**参考答案：**

尽量 additive：新增 optional fields/endpoints，保持旧语义；不兼容变更才 version，并配合明确 deprecation timeline、usage telemetry 和 migration guidance。

### Q86.2 — 内部机制

**问题：** 解释 additive change、deprecation、versioning、compatibility 和 schema evolution。

**参考答案：**

兼容性不仅是字段还存在，还包括 semantics。producer/consumer 可能跨多个版本同时运行，因此 schema evolution 必须支持混合阶段。

### Q86.3 — 生产场景

**问题：** 一个 field semantics 必须改变，但数千 clients 无法快速升级。怎么做？

**参考答案：**

新增一个表达新语义的 field/endpoint，或临时运行 compatibility layer/dual version，让旧 clients 继续工作，直到 usage 足够低。

### Q86.4 — 故障排查

**问题：** 如何测量 usage、roll out compatibility logic、检测 breakage 并 retire old contract？

**参考答案：**

按 client/version 记录调用量和 errors，用 contract tests/canary 验证，主动通知 consumers，并设定 sunset criteria；只有达到门槛才移除旧 contract。

### Q86.5 — Senior Trade-off / Edge Case

**问题：** 什么时候值得开一个新 API version，而不是继续 backward-compatible evolution？

**参考答案：**

如果旧/新语义无法清晰共存，或者保持兼容会长期扭曲设计，新 major version 才合理。普通 additive evolution 不应随便制造版本碎片。

---

## Q87 — REST → GraphQL Migration

### Q87.1 — 基础定义

**问题：** 如何从 REST 迁移到 GraphQL？

**参考答案：**

先在 REST 旁边增加 GraphQL endpoint，围绕 domain 定义 schema，resolver 复用现有 domain/service logic，逐步迁移 clients，最后基于 usage 决定是否 retire REST。

### Q87.2 — 内部机制

**问题：** 解释 schema、resolver、authorization、DataLoader/N+1、caching 和 query complexity。

**参考答案：**

GraphQL 给 client 灵活查询，但 resolver 很容易产生 N+1、复杂 auth、cache 困难和任意 query cost，因此需要 batching、complexity limit 和 observability。

### Q87.3 — 生产场景

**问题：** 已有 REST clients 无法同时迁移，如何安全双栈？

**参考答案：**

REST 与 GraphQL 应共享业务逻辑，避免复制两套规则。先迁移少量 read paths/clients，canary 比较 correctness 与 performance。

### Q87.4 — 故障排查

**问题：** 如何观察 resolver latency、query shape、backend load 和 regression？

**参考答案：**

监控 query depth/complexity、resolver timings/errors、backend call count、DataLoader/cache hit、DB load，并让 trace 能看到 resolver → dependency path。

### Q87.5 — Senior Trade-off / Edge Case

**问题：** 什么时候 GraphQL 其实不是好选择？

**参考答案：**

简单稳定 API、高度依赖 HTTP caching、文件传输，或团队没有 query-cost/security 能力时，GraphQL 可能增加的复杂度大于收益。

---

## Q88 — Technical Debt

### Q88.1 — 基础定义

**问题：** 如何判断 technical debt 值不值得修？

**参考答案：**

当持续持有 debt 的成本/风险超过修复成本时优先处理。证据包括 incidents、delivery delay、change failures、安全风险、cloud cost、重复 engineer time。

### Q88.2 — 内部机制

**问题：** 解释 debt principal、interest、risk 和 opportunity cost。

**参考答案：**

principal 是修复一次性成本；interest 是持续摩擦/风险；opportunity cost 是修它时放弃的其他价值。代码丑不等于高利息 debt。

### Q88.3 — 生产场景

**问题：** 团队想花 3 个月重写，因为当前 service “很乱”。如何评估？

**参考答案：**

要求说明重写解决哪些可量化问题、是否有 incremental alternative、migration risk、机会成本和成功指标。不能只因为“架构不好看”就重写。

### Q88.4 — 故障排查

**问题：** 如何利用 incident、change-failure、delivery 和 maintenance data 排优先级？

**参考答案：**

按 incident contribution、变更频率、lead time、regression、security/compliance、blast radius 和维护工时排序，先修 high-leverage bottleneck。

### Q88.5 — Senior Trade-off / Edge Case

**问题：** 什么时候主动保留 technical debt 是正确决定？

**参考答案：**

稳定、很少改、即将 retire、或当前业务机会更重要时，带着明确认知继续背 debt 完全合理；记录 revisit trigger 即可。

---

# 12 — 数据库 / 分布式系统

## Q89 — Database Suddenly Slow

### Q89.1 — 基础定义

**问题：** database-backed requests 突然变慢。你先查什么？

**参考答案：**

先判断是某些 queries 还是全库，再看 DB wait events、slow queries、locks、connections、CPU、buffer/cache、disk I/O、network 和最近 migration/data growth。

### Q89.2 — 内部机制

**问题：** 解释 query plan、locks、I/O、cache、connection pool、statistics 和 workload change。

**参考答案：**

bad plan/statistics、blocking locks、cold cache、storage latency、connection queue、资源 saturation 或 data distribution change 都会让 DB 变慢。

### Q89.3 — 生产场景

**问题：** DB CPU 很低但 latency 很高。哪些 hypotheses 更强？

**参考答案：**

低 CPU 更像等待而非计算：locks、disk/network I/O、connection acquisition、serialized contention、storage issue。

### Q89.4 — 故障排查

**问题：** 如何检查 waits、locks、disk latency、network、connection queue 和 recent schema/data changes？

**参考答案：**

查看 active sessions/waits、lock graph、`EXPLAIN ANALYZE`、I/O latency/queue、pool wait、slow-query log、network 和 deployment/migration timeline。

### Q89.5 — Senior Trade-off / Edge Case

**问题：** 事故中哪些 mitigation 较安全？哪些优化应该放到事后测试？

**参考答案：**

事故中可优先 rollback bad query/release、cancel runaway query、限流或暂时 scale；大型 index/schema 改造应先评估 lock/I/O 影响并在受控环境验证。

---

## Q90 — Database Index

### Q90.1 — 基础定义

**问题：** database index 到底做什么？

**参考答案：**

index 是辅助数据结构，用来通过 key 快速定位 rows，避免 full table scan。最常见是 B-tree，适合 equality/range/order 等访问模式。

### Q90.2 — 内部机制

**问题：** 解释 B-tree lookup、selectivity、ordering、write amplification 和 optimizer。

**参考答案：**

index 保存有序 key 和 row locator，optimizer 根据统计与成本决定是否使用；selectivity 低时 index 不一定划算。

### Q90.3 — 生产场景

**问题：** 为什么加 index 可能让 workload 更差？

**参考答案：**

每次 insert/update/delete 都可能维护 index，增加 storage、cache pressure、vacuum/maintenance 和 write latency；错误 index 还可能诱导 bad plan。

### Q90.4 — 故障排查

**问题：** 如何判断 slow query 是需要 index、改 query、更新 statistics 还是 schema change？

**参考答案：**

查看 `EXPLAIN/ANALYZE`、estimated/actual rows、filter/join/order pattern、已有 indexes、statistics freshness 和 workload frequency，再决定根因。

### Q90.5 — Senior Trade-off / Edge Case

**问题：** 如何在 read performance 与 write cost 之间设计 indexing strategy？

**参考答案：**

针对高价值 query patterns 建 index，合理设计 composite order，删除 unused/redundant indexes，并持续关注 write amplification 与数据分布变化后的 plan。

---

## Q91 — Database Connection Pool Exhaustion

### Q91.1 — 基础定义

**问题：** 为什么 DB CPU 很低，但应用会 timeout 等待 connections？

**参考答案：**

瓶颈可能在应用 pool，而不是 DB compute。所有 connections 被长时间持有、leak、长 transaction，或 pool 太小，caller 就会排队。

### Q91.2 — 内部机制

**问题：** 解释 pool sizing、connection leak、long transaction、queueing 和 DB max connections。

**参考答案：**

每个 app replica 都有自己的 pool；总潜在 connections = replica count × pool max。autoscaling 可能把 DB connection budget 直接放大。

### Q91.3 — 生产场景

**问题：** 所有 connections 都 busy，但 SQL 执行很快。这说明什么？

**参考答案：**

如果拿到 connection 后 query 很快，重点检查 connection hold time、transaction scope、在 transaction 中调用外部服务、leak 或 pool capacity，而不是 SQL CPU。

### Q91.4 — 故障排查

**问题：** 如何调查 pool wait、transaction duration、leak、thread dump 和 DB session state？

**参考答案：**

监控 active/idle/wait、checkout duration、acquisition timeout、DB active/idle-in-transaction sessions、long transactions 和 thread dumps/leak stack。

### Q91.5 — Senior Trade-off / Edge Case

**问题：** 多个 replicas 下如何正确设计 pool size？

**参考答案：**

先从 DB 安全承载 connections 的 global budget 出发，再分配给 replicas；pool 应 bounded，transaction 短，带 acquisition timeout/leak detection/backpressure。

---

## Q92 — Transaction Isolation

### Q92.1 — 基础定义

**问题：** transaction isolation levels 是什么？

**参考答案：**

isolation levels 定义并发 transaction 能看到哪些其他 transaction 的 effects，常见 Read Uncommitted、Read Committed、Repeatable Read/快照语义、Serializable。

### Q92.2 — 内部机制

**问题：** 解释 dirty read、non-repeatable read、phantom、snapshot/MVCC 与 serialization anomalies。

**参考答案：**

不同级别允许或阻止 dirty read、non-repeatable read、phantom、lost update、write skew 等；具体 MVCC 行为因数据库实现不同。

### Q92.3 — 生产场景

**问题：** 金融流程并发时偶尔 double-process，isolation 可能如何导致？

**参考答案：**

两个 transactions 可能都看到 `unprocessed`，然后各自执行 side effect。仅靠普通 read committed 可能无法阻止这种 business race。

### Q92.4 — 故障排查

**问题：** 如何复现并选择 stronger isolation、explicit lock、constraint 或 idempotency？

**参考答案：**

用并发 test 重现，明确 invariant，再测试 row lock、conditional update、unique constraint、higher isolation 或 idempotent state machine；同时验证 retry semantics。

### Q92.5 — Senior Trade-off / Edge Case

**问题：** 什么时候 serializable 值得付出 throughput/contention 成本？

**参考答案：**

当业务 invariant 必须看起来串行执行、冲突量可接受且应用能正确 retry serialization failure 时，Serializable 很有价值。

---

## Q93 — Redis / Cache Failure

### Q93.1 — 基础定义

**问题：** Redis 不可用时，整个 application 应该不可用吗？

**参考答案：**

取决于语义。如果 Redis 真的是可重建 cache，应用通常应该 degrade，而不是立刻全挂；但 fallback capacity 必须真实存在。

### Q93.2 — 内部机制

**问题：** 解释 cache-aside、TTL、fallback、cache miss 和 stampede。

**参考答案：**

cache-aside 在 miss 时去 source of truth。cache 全失时所有 request 同时 miss，会形成 stampede，把原本按高 hit-rate sizing 的 DB 压垮。

### Q93.3 — 生产场景

**问题：** cache outage 后所有流量打到 DB，DB 也挂了。发生了什么？

**参考答案：**

Redis 虽然逻辑上 optional，但架构把 DB capacity 建立在高 cache hit 上，所以 outage 直接产生 load amplification。

### Q93.4 — 故障排查

**问题：** 如何使用 stale reads、request coalescing、rate limit、circuit breaker 和 staged recovery？

**参考答案：**

可限流/丢弃非关键请求、允许安全 stale data、合并相同 key 的并发 miss、限制 DB concurrency，并逐步 warm cache 而不是瞬间恢复全部流量。

### Q93.5 — Senior Trade-off / Edge Case

**问题：** 什么时候一个“只是 cache”的 Redis 实际上已经是 critical dependency？

**参考答案：**

如果 origin 无法承担无 cache 流量，或 Redis 还存 session/lock/state，它就是 operationally critical dependency，架构与 SLO 应诚实反映。

---

## Q94 — Duplicate Messages

### Q94.1 — 基础定义

**问题：** consumer 收到同一 message 两次怎么办？

**参考答案：**

默认 message 可能重复。使用稳定 event/business ID，并让 consumer side effect idempotent，避免第二次处理产生重复业务影响。

### Q94.2 — 内部机制

**问题：** 解释 at-least-once、ack、deduplication、idempotent consumer 和 transactional outbox/inbox。

**参考答案：**

at-least-once 在 ack 丢失、consumer crash、rebalance 时会 redeliver；inbox/dedup table 或业务 unique constraint 用于记录已处理状态。

### Q94.3 — 生产场景

**问题：** 两个 duplicate messages 同时被不同 replicas 处理，需要什么保护？

**参考答案：**

必须有跨 replicas 的原子机制，如 DB unique constraint/conditional insert/transaction；单机 memory cache 不能保证。

### Q94.4 — 故障排查

**问题：** 如何处理 crash 发生在 side effect 与 ack 之间？

**参考答案：**

分析所有 crash point：side effect 前、side effect 后但 dedup/ack 前。使用同一 transaction、outbox/inbox、idempotent downstream 和 replay tests。

### Q94.5 — Senior Trade-off / Edge Case

**问题：** dedup key 的 retention window 和 key design 如何选择？

**参考答案：**

retention 至少覆盖 broker retry/replay 和业务重放窗口，key 应稳定且紧凑；超大长期 dedup state 要有 archive/expiry 策略。

---

## Q95 — Exactly-once Processing

### Q95.1 — 基础定义

**问题：** distributed messaging 真能保证 exactly-once processing 吗？

**参考答案：**

必须限定范围。broker 可以在自己的 transaction model 内提供 exactly-once semantics，但跨独立 DB/API 的 end-to-end business effect 通常无法自动 exactly once。

### Q95.2 — 内部机制

**问题：** 区分 exactly-once delivery、processing semantics 和 business effect。

**参考答案：**

delivery 是消息到达次数；processing 是 broker/consumer transaction；business effect 是外部世界的实际写入，这三个 guarantee 不等价。

### Q95.3 — 生产场景

**问题：** broker 声称 exactly-once，但 consumer 还写 external DB。duplicate 在哪里出现？

**参考答案：**

DB commit 成功后 consumer crash、但 broker offset 未 commit 时，message 会 replay，从而再次尝试 DB side effect。

### Q95.4 — 故障排查

**问题：** 如何跨 broker 与 DB failure boundary 设计 replay-safe workflow？

**参考答案：**

使用 idempotent DB writes、unique constraints、transactional outbox/inbox、broker transaction（适用时）和 reconciliation，并做 crash-point testing。

### Q95.5 — Senior Trade-off / Edge Case

**问题：** 什么时候 at-least-once + idempotency 反而更简单可靠？

**参考答案：**

异构系统中 at-least-once + 明确 idempotency 的边界更容易 reasoning 和验证。只有 broker exactly-once scope 真与业务 invariant 匹配时才值得依赖。

---

## Q96 — Cascading Failure

### Q96.1 — 基础定义

**问题：** Service D 变慢为什么最终可能把 Service A 也拖垮？

**参考答案：**

上游调用长期占住 threads/connections，queue 增长，caller timeout 后 retry，又增加下游 load，最终多个 service 的资源都被拖满。

### Q96.2 — 内部机制

**问题：** 解释 timeout stacking、pool exhaustion、retry amplification、queue growth 和 backpressure。

**参考答案：**

每一层的 wait、retry、queue 和 pool 会互相反馈。如果没有 backpressure/limits，局部 latency failure 会变成系统性 saturation。

### Q96.3 — 生产场景

**问题：** D 从 100ms 变成 3s 但没有完全 down，为什么反而危险？

**参考答案：**

hard failure 往往能快速触发 fail-fast/circuit breaker；3 秒的“慢成功”持续占用资源，可能在表面部分可用时悄悄毁掉 throughput。

### Q96.4 — 故障排查

**问题：** 如何用 timeout、bulkhead、circuit breaker、load shedding 和 bounded queue 控制 blast radius？

**参考答案：**

设置 bounded deadlines、circuit breaker、bulkheads/pool limits、load shedding、bounded queues 和 retry budget，并优先减少 offered load。

### Q96.5 — Senior Trade-off / Edge Case

**问题：** 多级 call chain 的 timeout budget 应如何分配？

**参考答案：**

从 end-to-end deadline 反推，内层 timeout 必须更短并留出处理/清理时间；只有 remaining deadline 和成功概率足够时才允许 retry。

---

# 13 — Incident Management

## Q97 — Production Outage 的第一步

### Q97.1 — 基础定义

**问题：** production down 了，你第一件事做什么？

**参考答案：**

先确认 customer impact 与 scope，判断 severity，明确 incident ownership/coordination，然后并行做安全 mitigation 和 evidence preservation。不要第一反应就随机翻日志。

### Q97.2 — 内部机制

**问题：** 解释 impact assessment、severity、ownership、stabilization、communication 和 evidence preservation。

**参考答案：**

事故需要知道谁受影响、从何时开始、谁做决策、谁调查、谁通信，以及当前最优先的 stabilization action。

### Q97.3 — 生产场景

**问题：** 多个 dashboards 都红，但 customer impact 不清楚。优先做什么？

**参考答案：**

先用真实 user journey、edge/SLO 和 synthetic check 确认外部症状，判断哪些 dashboard 红是 cause、effect 或 unrelated noise。

### Q97.4 — 故障排查

**问题：** 如何建立 scope、incident commander、timeline，并停止 uncoordinated changes？

**参考答案：**

declare incident，指定 IC 和 investigation owners，建立共享 timeline/command log，暂停非必要 production changes，并要求所有 mitigation 有 owner 和预期效果。

### Q97.5 — Senior Trade-off / Edge Case

**问题：** 怎样的 incident process 才既有结构又不会拖慢 responders？

**参考答案：**

流程应按 severity 伸缩。目的是减少重复劳动、冲突 change 和信息丢失，而不是让工程师在 outage 中填表。

---

## Q98 — Rollback 还是先 Debug

### Q98.1 — 基础定义

**问题：** release 后 error rate 立刻上升。先 rollback 还是先调查？

**参考答案：**

如果 release 相关性强、customer impact 大且 rollback 安全，应优先快速回退；若 rollback 本身可能破坏数据或相关性弱，则需要先验证或选择其他 mitigation。很多情况下可并行。

### Q98.2 — 内部机制

**问题：** 决策要考虑哪些因素？

**参考答案：**

考虑 SLO burn、blast radius、time-to-mitigate、rollback reversibility、schema/data compatibility、stateful side effects 和 confidence。

### Q98.3 — 生产场景

**问题：** release 包含 backward-incompatible DB migration，会如何影响？

**参考答案：**

旧 binary 可能无法读取新 schema，因此直接 rollback 可能二次故障。需要 forward fix、traffic reduction、feature disable，或先恢复 compatible schema。

### Q98.4 — 故障排查

**问题：** 如何在准备 rollback 的同时保留 evidence 和做 parallel diagnosis？

**参考答案：**

一人准备 rollback/runbook，其他人比较 old/new telemetry、migration/state、feature flag、dependency changes；同时保存 failing logs/traces/metrics。

### Q98.5 — Senior Trade-off / Edge Case

**问题：** deployment design 如何让 rollback 真正可靠？

**参考答案：**

使用 expand-contract migrations、feature flags、canary、immutable artifacts、version compatibility testing 和真正演练过的 rollback，而不是假设“旧版本一定能上”。

---

## Q99 — 未知 Root Cause 的 Incident

### Q99.1 — 基础定义

**问题：** 不知道 outage 原因时，你的 troubleshooting method 是什么？

**参考答案：**

先定义 symptom/scope/timeline，列出 recent changes，把系统划分 fault domains，按概率与影响排序 2–4 个 hypotheses，然后设计能区分它们的 tests，并优先 mitigation。

### Q99.2 — 内部机制

**问题：** 解释 symptom、scope、timeline、recent changes、fault-domain narrowing、hypothesis 和 evidence。

**参考答案：**

每个检查都应该回答一个问题，并改变某个 hypothesis 的 confidence；不断对比 failing 与 healthy dimensions，而不是全系统扫一遍。

### Q99.3 — 生产场景

**问题：** 前三个 hypotheses 都被证伪后，如何避免 random-walk debugging？

**参考答案：**

回到 assumptions 和时间线，找 known-good comparison，重新划分 fault domain，并选择信息增益最高的下一次测试。

### Q99.4 — 故障排查

**问题：** 如何使用 binary reduction、comparative analysis、controlled experiment 和 known-good baseline？

**参考答案：**

例如通过版本、AZ、tenant、node、dependency direct-call、synthetic path 等做二分，把“所有可能原因”快速缩到一小块。

### Q99.5 — Senior Trade-off / Edge Case

**问题：** 优秀 incident reasoning 与“会很多命令”最大的区别是什么？

**参考答案：**

强者能解释“为什么运行这个命令、不同结果分别说明什么、下一步是什么”；命令列表只是工具，真正核心是 causal model。

---

## Q100 — 多个相关 Symptoms

### Q100.1 — 基础定义

**问题：** CPU high、DB latency high、5xx 上升、queue depth 增长。哪个是 root cause？

**参考答案：**

仅凭四个同时红的指标无法确定 root cause，需要构建时间线和因果链。任何一个都可能是 cause 或 downstream effect。

### Q100.2 — 内部机制

**问题：** 为什么 correlation 不能直接证明 causality？

**参考答案：**

例如 DB 慢可让 queue 增；queue concurrency 也可让 DB 慢；retry 还可让 CPU 上升，所以同一时间窗口的相关性不等于因果。

### Q100.3 — 生产场景

**问题：** timeline 显示先 queue growth，再 DB latency，再 CPU。你会形成什么 hypothesis？

**参考答案：**

先 investigate 为什么 arrival rate 超过 service rate：traffic burst、consumer processing 变慢、downstream backpressure 或 stuck worker，然后看它是否解释后续 DB/CPU。

### Q100.4 — 故障排查

**问题：** 如何用 timestamps、telemetry 和 controlled mitigation 验证 causal chain？

**参考答案：**

对齐高分辨率 queue arrival/service rate、DB waits、retry count、CPU modes 和 change events，并尝试可逆 mitigation，如降低 concurrency，观察后续指标是否按预测恢复。

### Q100.5 — Senior Trade-off / Edge Case

**问题：** 如何在不确定时向 stakeholders 表达，而不是显得没判断力？

**参考答案：**

明确说：'当前 leading hypothesis 是 X，因为 A 先于 B，但尚未证明；下一步用 Y 验证。' 透明 uncertainty + test plan 比假装确定更专业。

---

## Q101 — 先 Mitigation 再永久修复

### Q101.1 — 基础定义

**问题：** 已知 likely root cause，但 permanent fix 需要两小时。怎么办？

**参考答案：**

优先用最安全、最快、可逆的方式降低 customer impact，同时并行开发永久 fix。事故目标首先是恢复稳定。

### Q101.2 — 内部机制

**问题：** 为什么 incident mitigation 与 root-cause fix 是两个目标？

**参考答案：**

mitigation 优化 time-to-stability；root-cause fix 优化长期 correctness。两者常不是同一个 change。

### Q101.3 — 生产场景

**问题：** 可以减流、disable feature 或加 capacity。如何选择？

**参考答案：**

按预期用户收益、执行速度、可逆性、confidence、data correctness risk 和 downstream capacity 选择最小有效动作。

### Q101.4 — 故障排查

**问题：** 执行 mitigation 前如何评估 risk、reversibility 和 secondary effects？

**参考答案：**

执行前明确 hypothesis、owner、预期 metric change、rollback plan 和 secondary risk；执行后立刻观察 SLO/error/latency/queue/capacity。

### Q101.5 — Senior Trade-off / Edge Case

**问题：** 什么条件说明可以从 incident response 转向 recovery/postmortem？

**参考答案：**

当用户影响消失或可接受、关键指标稳定一段时间、queue/resources 恢复且没有新的损害，再转入 recovery 和根因工作。

---

## Q102 — Rollback 后仍然故障

### Q102.1 — 基础定义

**问题：** release 导致 outage，但 rollback 后问题仍存在。下一步？

**参考答案：**

需要降低“新 binary 本身是唯一 root cause”的 confidence，调查 persistent state、schema、cache、queue、external dependency 或恰好同时发生的其他 failure。

### Q102.2 — 内部机制

**问题：** 哪些 irreversible side effects 会让旧版本也继续失败？

**参考答案：**

DB migration/data corruption、cache poisoning、queue backlog、feature flag/config、third-party state、infra mutation 都可能在 rollback binary 后继续存在。

### Q102.3 — 生产场景

**问题：** old version 现在和 new version 一样失败，这如何更新 hypothesis？

**参考答案：**

说明问题可能已经改变了 environment/state，或者 release 只是时间相关而非真正原因。应比较 state，不是继续反复 deploy。

### Q102.4 — 故障排查

**问题：** 如何比较 before/after state、migration、cache、queue、config 和 dependency？

**参考答案：**

重建精确 release timeline，对比 DB/schema/data、config/secrets、cache keys、queue state、infra changes 和 dependency metrics。

### Q102.5 — Senior Trade-off / Edge Case

**问题：** release controls 如何减少不可逆 changes？

**参考答案：**

采用 expand-contract schema、versioned config、feature flags、分阶段 stateful migration 和可验证的 rollback path，避免单次 release 同时做太多不可逆操作。

---

## Q103 — Partial Outage

### Q103.1 — 基础定义

**问题：** 只有 5% users 受影响。怎么调查？

**参考答案：**

partial outage 的核心是 segmentation：找受影响请求共享的最小维度，再与健康请求对比。

### Q103.2 — 内部机制

**问题：** 为什么 tenant、AZ、node、version、ISP、shard、endpoint 等 segmentation 很重要？

**参考答案：**

global average 会把局部问题隐藏；tenant、region/AZ、node/Pod、version、route、data partition、ISP 等都可能解释那 5%。

### Q103.3 — 生产场景

**问题：** 故障集中在一个 customer shard，但 global infra metrics 健康。查什么？

**参考答案：**

检查 shard DB/storage placement、routing/hash、leader/follower、capacity、data anomalies，以及与该 shard colocated 的 infrastructure/dependencies。

### Q103.4 — 故障排查

**问题：** 如何找到 affected users 的最小共同维度？

**参考答案：**

从 affected trace/log IDs 反查 version/node/shard/route，建立 healthy vs affected comparison，逐步找能最好区分两组请求的 dimension。

### Q103.5 — Senior Trade-off / Edge Case

**问题：** 怎样设计 telemetry，既支持 partial failure 调试又不爆 cardinality？

**参考答案：**

metric 使用 bounded dimensions；具体 customer/request ID 留在 logs/traces，并提供从 customer → shard/node/version 的可查询映射，而不是给每个 metric 加 customer_id。

---

## Q104 — Incident Commander

### Q104.1 — 基础定义

**问题：** Incident Commander 到底应该做什么？

**参考答案：**

IC 负责整体协调：明确 severity/scope、分配角色、维护优先级、控制变更风险、组织沟通，并持续判断 mitigation 是否有效。

### Q104.2 — 内部机制

**问题：** 解释 coordination、decision ownership、communication 和 role separation。

**参考答案：**

IC 应保留全局视角，不应被单个 hypothesis 吞没；需要追踪 decisions、owners、status cadence 和 blockers。

### Q104.3 — 生产场景

**问题：** IC 是现场技术最强的人，是否应该亲自做最深 debugging？

**参考答案：**

通常不建议。最强 SME 更适合深入调查；如果他同时当 IC，很容易 tunnel vision 并丢失 coordination。小事故可兼任，大事故应分离。

### Q104.4 — 故障排查

**问题：** 如何分配 operations、investigation、communications、scribe 和 SMEs？

**参考答案：**

根据规模设 IC、ops/mitigation lead、investigation SMEs、communications 和 scribe，一个人可在小事故中承担多个角色。

### Q104.5 — Senior Trade-off / Edge Case

**问题：** 怎样避免 IC 变成压制专业意见的 command-and-control？

**参考答案：**

IC 应主动邀请 evidence 和 dissent，复述决策依据，阻止互相冲突的 changes，并在证据变化时调整方向。权威用于组织专家，不是压制专家。

---

# 14 — Hands-on / Day in the Office

## Q105 — Broken Kubernetes Service — 12% HTTP 504

### Q105.1 — 基础定义

**问题：** ALB → Ingress → Service → Pods → PostgreSQL 链路中，12% requests 返回 504。你从哪里开始？

**参考答案：**

先量化 12% 的维度：时间、endpoint、version、Pod/node/AZ，并确认 504 是谁生成。然后选择一个 failing request 做端到端 trace。

### Q105.2 — 内部机制

**问题：** 如何画出每一 hop 的 timeout budget 与 observable boundary？

**参考答案：**

列出 client/LB/ingress/app/DB connect/request/idle deadlines，并标明每层可看到的 timing/log/metric。504 本质是某中间层等待 upstream 超过 deadline。

### Q105.3 — 生产场景

**问题：** 故障只集中在两个 Pods。你会和健康 Pods 比什么？

**参考答案：**

比较 node、CPU throttling、GC/memory、connection pools、network、config/version、DB sessions、dependency timing；必要时先把异常 Pods 移出流量。

### Q105.4 — 故障排查

**问题：** 如何使用 metrics、logs、traces、Endpoint state、node/network 和 DB sessions 找到 bottleneck？

**参考答案：**

结合 ALB/ingress timing logs、trace spans、EndpointSlices、per-Pod metrics、node/CNI packet data、DB waits/connections 和从不同网络位置的 targeted `curl`。

### Q105.5 — Senior Trade-off / Edge Case

**问题：** mitigation 后应加入哪些 design、alert 和 test，避免同类 partial timeout 再出现？

**参考答案：**

加入 per-instance tail-latency/outlier detection、明确 timeout budget、connection-pool telemetry、canary/load test，以及自动 traffic removal/rollout abort。

---

## Q106 — Release Regression Timeline

### Q106.1 — 基础定义

**问题：** 13:04 deploy，13:08 errors 上升，13:10 CPU 上升，13:12 queue depth 上升。你如何解释？

**参考答案：**

时间顺序更像 release 引入 latency/error，CPU 和 queue 可能是后果。先不要因为 CPU 最红就认为 CPU 是 root cause。

### Q106.2 — 内部机制

**问题：** 如何构建 causal chain，而不是把四个 metrics 当独立问题？

**参考答案：**

可能链路是：new request 更慢 → concurrency 上升 → CPU 增加 → service rate 下降 → queue 增长 → retry 再放大。

### Q106.3 — 生产场景

**问题：** 只有 new version 的 request duration 先变高。leading hypothesis 是什么？

**参考答案：**

优先怀疑新版本 code path、serialization、lock、dependency call 或配置造成 per-request work/latency 增加。

### Q106.4 — 故障排查

**问题：** 如何验证是慢代码导致 queue/CPU amplification，还是 external dependency 先变慢？

**参考答案：**

对比 old/new version traces、dependency spans、CPU profile、queue arrival/service rate、retry 和 resource use；安全 rollback/canary shift 后观察整条链是否逆转。

### Q106.5 — Senior Trade-off / Edge Case

**问题：** 哪些 release guardrails 可以在 full rollout 前阻止它？

**参考答案：**

progressive delivery、version-scoped metrics、error/latency/SLO burn 自动 gate、representative canary traffic，以及 queue/saturation guardrail。

---

## Q107 — Intermittent DNS — 2% Failure

### Q107.1 — 基础定义

**问题：** 98% requests 成功，2% 报 `Temporary failure in name resolution`。第一步怎么查？

**参考答案：**

按 node/Pod/resolver/hostname/time/query type 分组，并确认应用真正收到的 resolver error 和 retry 次数。

### Q107.2 — 内部机制

**问题：** 哪些 DNS layers 和 retry/caching behaviour 可以产生间歇 failure？

**参考答案：**

可能涉及 Pod stub resolver、search domains/`ndots`、CoreDNS Service/datapath、node conntrack、upstream resolver、authoritative DNS 和 UDP loss/TCP fallback。

### Q107.3 — 生产场景

**问题：** 故障集中在一部分 Kubernetes nodes。什么原因更可能？

**参考答案：**

node-local CNI/network、conntrack saturation、resolver config 或到 CoreDNS 的路径更可疑，而不是 global DNS record。

### Q107.4 — 故障排查

**问题：** 如何检查 CoreDNS、node resolver path、conntrack、packet loss、`ndots` 和 upstream DNS？

**参考答案：**

重复 `dig` cluster DNS/upstream，检查 `/etc/resolv.conf`、CoreDNS metrics/logs、node conntrack/drops，并在受影响 node 抓 UDP/TCP 53 traffic。

### Q107.5 — Senior Trade-off / Edge Case

**问题：** 如何提高 resilience，同时避免过度 caching/stale discovery？

**参考答案：**

合理扩容/cache、NodeLocal DNS、减少不必要 search queries、bounded application retry，并保持 TTL 足够短以支持动态 service discovery。

---

## Q108 — 一个坏 Kubernetes Node

### Q108.1 — 基础定义

**问题：** 只有某个 worker node 上的 Pods 都很慢。怎么办？

**参考答案：**

如果用户受影响先 cordon，阻止新 Pod 调度，然后比较该 node 的 CPU modes、disk、network、kernel、CNI、runtime、conntrack、hardware/cloud health 与健康 node。

### Q108.2 — 内部机制

**问题：** 哪些 node-local resources/datapaths 会造成这种问题？

**参考答案：**

所有 Pods 共享 host kernel、NIC、routes、CNI state、conntrack、runtime 和部分 storage path，因此 node-local degradation 会跨服务出现。

### Q108.3 — 生产场景

**问题：** CPU/memory 正常，但所有 Pods 都 network latency 高。下一步？

**参考答案：**

重点看 NIC drops/errors、softirq/interrupt saturation、CNI overlay、routes、MTU/fragmentation、conntrack 和 underlying host/network。

### Q108.4 — 故障排查

**问题：** 如何检查 NIC、CNI、route、MTU、conntrack、kernel logs 和 interrupts？

**参考答案：**

使用 `ip -s link`、`ethtool`（可用时）、`ss`、conntrack stats、routes、CNI/BPF/iptables、`dmesg`、softirq metrics 和 packet capture，并与健康 node 对比。

### Q108.5 — Senior Trade-off / Edge Case

**问题：** 什么时候应立即 cordon/drain，什么时候值得继续在线调查？

**参考答案：**

若 node 正在伤害用户且 redundancy 足够，优先 drain；若影响可控且证据易消失，可短暂保留调查。availability 永远优先于 forensic curiosity。

---

## Q109 — JVM Heap 60%，Container Memory 98%

### Q109.1 — 基础定义

**问题：** JVM heap 60%，container memory 98%。有哪些原因？

**参考答案：**

`-Xmx` 只约束 heap。总 container memory 还包括 metaspace、code cache、direct buffers、native/JNI、thread stacks、allocator overhead、mmap 和 cache。

### Q109.2 — 内部机制

**问题：** 拆解 native memory、metaspace、direct buffer、thread stack、code cache、mmap 和 page cache。

**参考答案：**

高 thread 数会增加 stacks，Netty/direct I/O 会使用 off-heap buffers，native libs 与 mmap 也不受 heap GC 直接控制。

### Q109.3 — 生产场景

**问题：** GC metrics 正常但 Pod 快 OOM。收集什么 evidence？

**参考答案：**

查看 JVM NMT、cgroup `memory.current/stat`、`/proc/<pid>/smaps`、thread count、direct-buffer metrics、mappings 和历史趋势。

### Q109.4 — 故障排查

**问题：** 如何结合 NMT、`/proc`、cgroup counters、thread count、direct-buffer usage？

**参考答案：**

使用 `jcmd VM.native_memory`、heap info、thread dump、cgroup stats，把 JVM 看到的各区域与 OS/cgroup total 对齐，找出差额来源。

### Q109.5 — Senior Trade-off / Edge Case

**问题：** 如何建立 Java-on-Kubernetes memory budget policy？

**参考答案：**

基于真实 load 为 heap 之外留固定/比例 headroom，限制 thread/direct memory，监控 cgroup working set/headroom，并在 production-like concurrency 下验证。

---

## Q110 — 所有 Services Healthy，但 User Journey 失败

### Q110.1 — 基础定义

**问题：** A、B、C 三个 services 都 healthy，但 end-to-end user journey 失败。为什么可能？

**参考答案：**

local health 只说明单组件能完成某个 probe，不代表跨服务 contract、auth、routing、data state 和 sequencing 正确。

### Q110.2 — 内部机制

**问题：** 解释 component health 与 interaction health 的区别。

**参考答案：**

A 可以访问 DB、B 可以返回 `/health`、C 可以 Ready，但 A→B→C 的 schema/version、token claims 或 state machine 仍可能不兼容。

### Q110.3 — 生产场景

**问题：** 每个 service health check 都通过，但组合 transaction error。需要什么 evidence？

**参考答案：**

需要一个真实 failing transaction 的 trace/correlation ID，并找一个同类型 successful transaction 做对比，定位第一次 behaviour divergence。

### Q110.4 — 故障排查

**问题：** 如何检查 trace、contract、auth context、state transition 和 timing？

**参考答案：**

查看 distributed trace、structured business events、request/response schema、auth/token claims、routing/version、state transition records 和 timestamps。

### Q110.5 — Senior Trade-off / Edge Case

**问题：** 应加入什么 synthetic/business transaction check？

**参考答案：**

对关键用户流程建立 end-to-end synthetic transaction/business SLI；component probes 继续服务 orchestration，但产品 availability 必须在用户 outcome 边界测量。

---

## Q111 — 危险的 Terraform Apply

### Q111.1 — 基础定义

**问题：** 接手 terminal 时发现 `terraform apply` 准备 recreate 一个关键 production resource。第一步做什么？

**参考答案：**

如果尚未执行 destructive action，先停止/暂停，确认 apply 状态，保存当前 plan/state/terminal context，并确认资源业务 criticality。不要再随手运行一次 apply。

### Q111.2 — 内部机制

**问题：** replacement 通常由哪些 Terraform/provider 机制触发？

**参考答案：**

ForceNew attributes、resource address/state mismatch、provider version behaviour、lifecycle、dependency change 都可能触发 replacement。

### Q111.3 — 生产场景

**问题：** 如果某 attribute 看起来可以手工改来避免 replacement，应该直接改吗？

**参考答案：**

不要为了避 replace 就直接 console 改。手工 drift 可能让下一次 plan 更危险；先确认 provider 是否支持 in-place change，以及 state 如何 reconcile。

### Q111.4 — 故障排查

**问题：** 如何安全 stop、保存 plan/state、比较 real infra 并找 recovery path？

**参考答案：**

保存 saved plan、备份 state、冻结其他 writers，对比 config/state/real resource/provider docs，找出 replacement trigger，再通过 config fix/import/state move 等方式生成新的 reviewed plan。

### Q111.5 — Senior Trade-off / Edge Case

**问题：** 怎样的组织 controls 能让 accidental destructive apply 极难发生？

**参考答案：**

CI-only apply、critical destroy/replace policy gate、saved-plan approval、state protection、least privilege、owner approval、canary stack 和 break-glass 流程。

---

## Q112 — Database Connection Exhaustion Hands-on

### Q112.1 — 基础定义

**问题：** DB CPU 30%、connections 100%、app latency 15 秒。第一 hypothesis 是什么？

**参考答案：**

首要 hypothesis 是应用 connection pool/DB connection budget 已耗尽，请求主要时间花在等 connection，而不是执行 SQL。

### Q112.2 — 内部机制

**问题：** 为什么 pool exhaustion/queueing 可以发生而 DB CPU 不高？

**参考答案：**

DB CPU 只反映正在执行的 work。caller 在应用 pool queue 中等待时并不会增加 DB CPU；long-held/leaked connections 也可能让 DB 看起来很闲。

### Q112.3 — 生产场景

**问题：** 一旦拿到 connection，queries 都很快。哪些 application behaviours 更可疑？

**参考答案：**

检查长 transaction、connection leak、在持有 connection 时做外部 I/O、连接没及时 return、pool 配置过小，或 replica autoscaling 导致总连接数过多。

### Q112.4 — 故障排查

**问题：** 如何检查 pool wait、leak/long-held connections、transactions、thread dumps 和 replica count？

**参考答案：**

看 pool active/idle/wait/acquisition time、connection hold time、DB session states、idle-in-transaction、thread dumps、replica count × pool max 和 leak traces。

### Q112.5 — Senior Trade-off / Edge Case

**问题：** 哪些 pool sizing、timeout、leak detection 和 backpressure policy 可以防止 cascade？

**参考答案：**

从 DB global connection budget 反推每 replica pool，设置 bounded pool、short transaction、acquisition/query timeout、leak detection 和 backpressure；autoscaling 不应无限放大 DB connections。

---

# 15 — SRE Scripting 与 Operational Coding

## Q113 — Bash 执行模型与安全 Shell 设置

### Q113.1 — 基础定义

**问题：** Bash 中 exit code、`set -e`、`set -u`、`set -o pipefail` 分别做什么？

**参考答案：**

Unix process 通常以 0 表示成功、非 0 表示失败。`set -e` 会在很多未处理失败时退出，`set -u` 把未定义变量当错误，`set -o pipefail` 让 pipeline 中任一命令失败都能使整个 pipeline 失败，而不是只看最后一个命令。

### Q113.2 — 内部机制

**问题：** 为什么 `set -e` 在 conditionals、pipelines、subshells 和 command substitutions 中仍可能出现反直觉行为？

**参考答案：**

`set -e` 的行为依赖语法上下文。Bash 在某些用于判断状态的 construct 中会抑制它，subshell 和 command substitution 也可能改变传播语义，所以它不能替代关键操作的显式错误检查。

### Q113.3 — 生产场景

**问题：** 你接手一个 deployment script，某个命令失败后脚本仍然静默继续。如何让 failure handling 更明确、更可观测？

**参考答案：**

可使用合适的 strict mode，同时对 destructive steps 做显式 validation，错误写到 stderr，加入结构化日志和 `trap` cleanup。关键命令应明确 retry、abort 或 compensate，而不是完全依赖 shell 隐式退出规则。

### Q113.4 — 故障排查

**问题：** 一个 Bash script 本地正常，但 CI 中偶发提前退出。你会如何 debug？

**参考答案：**

比较 shell version、environment variables、working directory、permissions、PATH、mounted files、secrets 和 runner image。必要时使用 `set -x`，但先确保不会泄露 secrets，并尽量在相同 CI image/container 中复现。

### Q113.5 — Senior Trade-off / Edge Case

**问题：** 哪些 shell scripting practices 值得标准化？什么时候应该停止用 Bash，改用通用编程语言？

**参考答案：**

标准化 quoting、严格变量处理、functions、readonly config、安全 tempfile、trap、bounded retry 和 ShellCheck。Bash 适合 orchestration；一旦逻辑变成 stateful、concurrent、测试复杂或数据结构较重，Python/Go/Java 更安全。

---

## Q114 — grep / awk / sed / jq / xargs 流式处理

### Q114.1 — 基础定义

**问题：** 在 SRE workflow 中，什么时候分别使用 `grep`、`awk`、`sed`、`jq` 和 `xargs`？

**参考答案：**

`grep` 适合过滤文本，`awk` 适合按字段解析和聚合，`sed` 适合流式文本变换，`jq` 适合结构化 JSON，`xargs` 把输入转成命令参数或受控并行任务。应根据数据格式和 correctness 要求选工具。

### Q114.2 — 内部机制

**问题：** 为什么 Unix pipeline 可以处理大文件而不一次性加载到内存？哪些地方仍可能因为 buffering 出问题？

**参考答案：**

多数 Unix text tools 是逐行/逐块 streaming，所以 memory 可以保持有界。但 sort、grouping、某些 `awk` map、应用 buffering 或 downstream command 仍可能积累大量状态；producer 比 consumer 快时 pipe 也会阻塞。

### Q114.3 — 生产场景

**问题：** 有一个 50GB log file，需要找出 error 最多的 endpoints，同时不能 OOM。你怎么做？

**参考答案：**

先尽早 filter，只解析必要字段，按 bounded endpoint key 做计数，再只对较小 summary 排序。结构化日志优先 `jq` 或专门脚本，而不是 regex。若 endpoint cardinality 极高，可使用 external sort 或 disk-backed aggregation。

### Q114.4 — 故障排查

**问题：** 一个 pipeline 遇到包含空格、换行或特殊字符的 filename 就坏了，怎么修？

**参考答案：**

使用 NUL-delimited 方式，如 `find -print0` 配 `xargs -0`，变量 expansion 始终 quote，不要 parse `ls`，把 path 当 data 而不是 shell syntax。JSON 应交给 `jq` 解析。

### Q114.5 — Senior Trade-off / Edge Case

**问题：** 复杂 one-liner 与小型 tested script 在 correctness 和 maintainability 上怎么取舍？

**参考答案：**

one-liner 很适合 incident triage 和探索，但太密的 shell pipeline 难测试、难 review、错误处理弱。只要成为 business-critical 或包含复杂 branching/state，就应升级成可读、可测试程序。

---

## Q115 — Process、Signal、Pipe 与 Subprocess 控制

### Q115.1 — 基础定义

**问题：** Unix shell 中 foreground jobs、background jobs、pipes、process groups 和 signals 是什么关系？

**参考答案：**

shell 会创建 processes，并常把一个 pipeline 的相关 processes 放入同一 process group。foreground group 会接收 terminal signals；pipe 把一个 process 的 stdout 接到另一个 stdin；signals 用于异步控制，如 TERM、INT。

### Q115.2 — 内部机制

**问题：** pipeline 中一个 process 提前退出，而另一个还在写，会发生什么？

**参考答案：**

如果 reader 先退出，writer 之后写 pipe 可能收到 SIGPIPE 或 EPIPE；如果 writer 先结束，等所有 write descriptors 关闭后 reader 会看到 EOF。不能假设 pipeline 所有 component 都成功完成。

### Q115.3 — 生产场景

**问题：** automation script 在 deployment 中被 terminate，但它启动的 child processes 还在运行，会有什么风险？

**参考答案：**

children 可能继续修改 infrastructure、持有 locks 或留下 partial state。parent 应定义清楚 ownership、signal forwarding、wait/reap 和 completion state，不能让 child 成为无人管理的副作用。

### Q115.4 — 故障排查

**问题：** 一个 subprocess 看起来 hang，但 parent 仍正常运行。怎么诊断？

**参考答案：**

用 `ps`、`pstree`、`/proc`、必要时 `strace`，查看 open FDs、wait channel 和 child states，判断是在等 I/O、child、lock、pipe、DNS/network，还是某个没有 timeout 的外部命令。

### Q115.5 — Senior Trade-off / Edge Case

**问题：** production automation 应如何管理 cancellation、timeouts、cleanup 和 process ownership？

**参考答案：**

使用 explicit deadlines、process groups、先 TERM 后 bounded KILL、trap/finally cleanup、idempotent cleanup，并对 destructive workflow 保存 durable progress state。取消后系统应处于可恢复状态。

---

## Q116 — 可靠 SRE Automation：Retry、Timeout、Locking、Idempotency

### Q116.1 — 基础定义

**问题：** 什么特征让一个 operational script 可以安全 rerun？

**参考答案：**

安全 rerun 的工具应有显式 preconditions、idempotent 或可 deduplicate side effects、确定性 inputs、清晰 state transitions，并在 mutate 前读取当前 remote state。重复执行应趋向同一 desired state，而不是重复副作用。

### Q116.2 — 内部机制

**问题：** retry、timeout、jitter 和 idempotency 在 automation 中如何互相作用？

**参考答案：**

timeout 限制等待，retry 应对 transient failure，jitter 防止同步重试，idempotency 让 retry 安全。若 non-idempotent request 出现 ambiguous timeout，必须用 request key、state query 或 reconciliation 避免重复副作用。

### Q116.3 — 生产场景

**问题：** 同一个 maintenance job 可能并发运行两个实例，如何避免 unsafe overlap？

**参考答案：**

优先采用单 scheduler/queue ownership。如果必须协调，可使用具有 ownership/expiry semantics 的可靠锁，同时让受保护操作本身尽量能抵抗 stale holder。

### Q116.4 — 故障排查

**问题：** 脚本调用 cloud API 后 timeout，但不知道 remote operation 是否成功。怎么安全恢复？

**参考答案：**

把结果视为 unknown，而不是 failed。使用稳定 operation/resource ID 查询 provider 真实状态，对比 desired vs actual，再决定继续、adopt 已完成 operation 或 compensate，绝不能盲目重复不可逆请求。

### Q116.5 — Senior Trade-off / Edge Case

**问题：** production-grade automation 与 throwaway script 的核心区别是什么？

**参考答案：**

production tool 应有 structured logs、合理的 dry-run、tests、bounded concurrency、timeouts、retries、idempotency、明确 exit codes、metrics/auditability、least privilege 和 documented recovery path。

---

## Q117 — Operational Coding 的数据结构与复杂度

### Q117.1 — 基础定义

**问题：** SRE 在 coding interview 和 automation 中至少应熟悉哪些基础数据结构？

**参考答案：**

至少应熟悉 array/list、hash map/dictionary、set、queue/deque、stack、heap/priority queue，以及基础 tree/graph。重点不是背定义，而是根据 lookup、ordering、dedup 和 streaming 需求选择结构。

### Q117.2 — 内部机制

**问题：** time/space complexity 如何影响处理百万级 records 的 operational tool？

**参考答案：**

O(n²) 在数据量扩大后可能完全不可用，而不必要保留 O(n) 状态也可能造成 memory pressure。operational program 应先估计输入规模，再决定是否需要 streaming/bounded-memory。

### Q117.3 — 生产场景

**问题：** 需要找出 error 最多的 top 100 hosts，你会选择什么数据结构？

**参考答案：**

若 host cardinality 可控，可用 hash map 保存 host→count，再用大小为 100 的 min-heap 保留 top K。若 cardinality 极大，应考虑 external aggregation 或 approximate heavy-hitter 方法。

### Q117.4 — 故障排查

**问题：** 脚本从 1 万 records 扩到 1000 万后突然很慢，如何判断是不是 algorithmic complexity？

**参考答案：**

对多个输入规模做 benchmark/profile，观察增长曲线，重点检查 nested scans、重复 sort、string copying、network round trips 和 data structure 操作，而不是先做低层微优化。

### Q117.5 — Senior Trade-off / Edge Case

**问题：** 什么时候理论上更优的 algorithm 反而不是 production 最佳选择？

**参考答案：**

当数据规模小、实现复杂度和 correctness risk 更高、成熟 library 已足够时，简单 O(n log n) 可能优于复杂 O(n)。production 优化的是总风险和总成本，不是只追 Big-O。

---

## Q118 — 超大文件 Streaming 与 OOM 避免

### Q118.1 — 基础定义

**问题：** 如何处理比可用内存更大的文件？

**参考答案：**

按 bounded chunks/records 增量读取，每条处理完及时释放，只保存必要且有界的 aggregate state，不把原始数据全留在内存。

### Q118.2 — 内部机制

**问题：** streaming、memory mapping 和一次性 load 整个 file 有什么区别？

**参考答案：**

整文件 load 会把全部内容 materialize 到 process memory；streaming 只保留当前 chunk；mmap 让 OS 按需 page file regions，但 address space 和 page cache 仍可能很大，并不等于自动 bounded memory。

### Q118.3 — 生产场景

**问题：** 要解析 200GB newline-delimited JSON 并统计每日 errors，怎么设计？

**参考答案：**

逐行读取、结构化解析 JSON，只按 day/status 等有界 key 聚合，周期 checkpoint 并输出 partial result。并行度由 CPU/I/O 决定，同时要保证 record boundary correctness。

### Q118.4 — 故障排查

**问题：** 程序明明是 streaming，但运行几小时后 memory 仍缓慢增长。查什么？

**参考答案：**

检查 unbounded map/set、保留 parsed objects、cache、exception buffers、logging queue、closure capture、allocator fragmentation，以及某些 library 是否内部批量缓存。

### Q118.5 — Senior Trade-off / Edge Case

**问题：** 如何让任务失败后可以 resume，而不是从头重跑？

**参考答案：**

保存 checkpoint，例如 byte offset + source identity/hash，或把输入切成 immutable chunks 并记录 completion markers。输出要 idempotent，重跑某 chunk 不应 double-count。

---

## Q119 — 可靠的 HTTP API Client

### Q119.1 — 基础定义

**问题：** production SRE HTTP client 除了“能调用接口”之外还应该处理什么？

**参考答案：**

应验证 TLS、使用 bounded timeouts、连接池、明确 redirect 行为、解析 errors、尊重 rate limits、只 retry 安全 transient failures、支持 idempotency，并输出 metrics/logs，同时保护 credentials。

### Q119.2 — 内部机制

**问题：** connect timeout、read timeout、total deadline、retry、connection pooling 有什么区别？

**参考答案：**

connect timeout 限制建立连接时间；read timeout 限制等待数据；total deadline 限制整个 logical operation 包括 retries；pooling 复用连接，减少 TCP/TLS handshake。

### Q119.3 — 生产场景

**问题：** automation client 面对 rate-limited API，应如何处理 429 和 transient 5xx？

**参考答案：**

尊重 `Retry-After`（若提供），使用 exponential backoff + jitter，限制 attempts 和总 deadline，并降低 concurrency。只 retry 已知 safe 或有 idempotency key 保护的操作。

### Q119.4 — 故障排查

**问题：** client 在 timeout 后偶尔创建 duplicate resources，如何诊断并修复？

**参考答案：**

第一次 request 可能已成功，只是 response 丢了。通过 request ID、audit log 和 server-side resource state 关联，增加稳定 idempotency key 或 query-before-retry/reconciliation。

### Q119.5 — Senior Trade-off / Edge Case

**问题：** 如何安全管理 credentials，并让 client 可观测但不泄露 secrets？

**参考答案：**

使用 secret/identity provider 的短期 credentials，不记录 token，redact 敏感 headers/body，只记录 request ID/status/latency，并暴露 retry/rate-limit metrics。TLS verification 不应被关闭。

---

## Q120 — Worker Pool、Rate Limit 与 Backpressure

### Q120.1 — 基础定义

**问题：** 为什么 bounded worker pool 通常比每个 task 创建一个 thread/process 更安全？

**参考答案：**

bounded pool 可以限制 CPU、memory、file descriptors 和 downstream concurrency。unbounded spawning 会把 load spike 直接转化为资源耗尽。

### Q120.2 — 内部机制

**问题：** concurrency limiting、rate limiting 和 backpressure 有什么区别？

**参考答案：**

concurrency limit 控制同时 in-flight 数量；rate limit 控制单位时间工作量；backpressure 把 downstream saturation 反馈给 producer，让它减速或保持 queue 有界。

### Q120.3 — 生产场景

**问题：** maintenance tool 的处理速度快于 downstream API 限制，worker model 怎么设计？

**参考答案：**

使用 bounded queue/worker count、token bucket/leaky bucket rate limiter、per-request deadline、带 jitter 的 retry scheduling，并监控 queue age、throughput、errors 和 429。

### Q120.4 — 故障排查

**问题：** queue 长度持续增长，但 workers 看起来健康。如何决定 scale、throttle producers 还是 shed work？

**参考答案：**

比较 arrival rate、service rate、queue age、worker utilization、downstream saturation 和业务 deadline。只有 downstream 还有容量时 scale，否则应减 producer 或 defer/shed 低优先级任务。

### Q120.5 — Senior Trade-off / Edge Case

**问题：** 给 worker pool 加 retry 后会出现哪些 failure modes？

**参考答案：**

retry 可能占满 workers、改变顺序、放大 queue pressure 并饿死新任务。应使用独立 delayed retry scheduling、retry budget、DLQ，并确保 task idempotent。

---

## Q121 — Operational CLI 的 Testing 与 UX

### Q121.1 — 基础定义

**问题：** 如何组织一个小型 production CLI，使其容易测试？

**参考答案：**

把 argument parsing、domain logic、side-effect adapters 和 output formatting 分离。核心逻辑尽量 deterministic，可注入 clock/client，深层函数返回 errors，而不是直接 `exit`。

### Q121.2 — 内部机制

**问题：** 哪些部分适合 unit test，哪些适合 integration test？

**参考答案：**

unit test 解析、validation、state transitions、retry decision 和纯逻辑；integration test 真实 serialization、auth、network/API contract、filesystem 和关键 provider interactions。

### Q121.3 — 生产场景

**问题：** CLI 在 mocks 下通过，但真实 cloud API 失败，说明什么？

**参考答案：**

说明 mock 模型不真实，可能漏掉 auth、pagination、eventual consistency、rate limit 或 error semantics。应补 contract/integration coverage，而不是把 mock 做得更复杂。

### Q121.4 — 故障排查

**问题：** 如何测试 timeout、partial success、retry、SIGTERM 和 duplicate execution？

**参考答案：**

使用 fault injection/fake servers 控制 deterministic failure，真实环境验证 provider semantics，process-level test 测 signal，replay test 验证 idempotency，并同时断言 final state、exit code 和 logs。

### Q121.5 — Senior Trade-off / Edge Case

**问题：** 危险 operations CLI 应具备哪些 human-safety UX？

**参考答案：**

提供合理 `--dry-run`、明确 target/environment、destructive confirmation、machine-readable output、非 0 exit codes、stable idempotency keys、完整 audit log，且绝不能默认指向 production。

---

# 16 — CI/CD 与 Release Engineering

## Q122 — GitHub Actions Workflow、Jobs、Runners 与 Dependencies

### Q122.1 — 基础定义

**问题：** 解释 GitHub Actions workflow 的 execution model：events、jobs、steps、runners 和 artifacts。

**参考答案：**

workflow 由 event 触发，包含一个或多个 jobs；每个 job 在 runner 上执行 steps。默认 jobs 可并行，artifact 用于在 job/run 之间保存明确 build outputs。

### Q122.2 — 内部机制

**问题：** job dependencies、matrix、cache、artifact 和 reusable workflow 分别有什么作用？

**参考答案：**

`needs` 定义 job dependency；matrix 展开参数组合；cache 用于加速 dependencies，但不应当成可信 artifact；artifact 是明确输出；reusable workflow 用于复用 pipeline logic。

### Q122.3 — 生产场景

**问题：** workflow 是 green，但部署 artifact 不是 review 的那个 commit。可能发生了什么？

**参考答案：**

可能是 deploy stage 又 rebuild、mutable tag、checkout 错 SHA/ref、artifact name collision、cache 被误用、un-pinned dependency，或 promotion 下载了另一个 run 的 artifact。应 build once，以 immutable digest promotion。

### Q122.4 — 故障排查

**问题：** self-hosted runner 偶尔残留 state 污染后续 builds。怎么调查和缓解？

**参考答案：**

检查 workspace cleanup、Docker/container state、tool cache、credentials、background processes、permissions 和 runner lifecycle。高隔离场景更适合 ephemeral runners，否则必须有 deterministic cleanup 和 health checks。

### Q122.5 — Senior Trade-off / Edge Case

**问题：** production delivery system 中什么时候选 hosted runners，什么时候选 self-hosted runners？

**参考答案：**

hosted runners 维护成本低、ephemeral；self-hosted 可访问 private network、自定义硬件/工具或优化成本，但需要自己承担 patching、isolation、capacity 和 supply-chain security。

---

## Q123 — GitHub Actions Permissions、OIDC、Secrets 与 Deployment Concurrency

### Q123.1 — 基础定义

**问题：** `GITHUB_TOKEN` permissions 和 workflow/repository 权限应该如何设计？

**参考答案：**

默认 least privilege，只给 job 所需权限，最好在 job scope 明确声明。build/read 与 deploy/write 权限分开，不要在 untrusted code path 中提供 broad write token。

### Q123.2 — 内部机制

**问题：** 为什么 OIDC federation 通常比 CI 中长期 cloud access keys 更安全？

**参考答案：**

OIDC 让 runner 用签名 workload identity 换取短期 cloud credentials，不必在 CI 存 static keys，并可按 repo、branch、environment、workflow claims 限制 trust。

### Q123.3 — 生产场景

**问题：** 一个 PR workflow 可以执行不可信代码，同时读取 production secrets。问题是什么？

**参考答案：**

不可信 PR 代码可以直接 exfiltrate secrets 或篡改 deployment 行为。生产 credentials 不应暴露给 arbitrary fork/PR execution，应分离 trusted deployment workflow 和 protected environment。

### Q123.4 — 故障排查

**问题：** 两个 production deployment workflows 并发运行发生 race，如何防止？

**参考答案：**

使用 concurrency groups 或外部 deployment lock，保证同一 target environment 只有一个 mutating deploy；明确 queue/cancel semantics，并让 deployment 本身 idempotent。

### Q123.5 — Senior Trade-off / Edge Case

**问题：** 对 production environments、approvals、secrets 和 third-party actions 应加什么 controls？

**参考答案：**

使用 protected environments、required reviewers、least-privilege OIDC roles、secret scoping/rotation、第三方 action pin SHA、dependency review、audit logs，并分离 build provenance 与 deploy authorization。

---

## Q124 — Jenkins Controller、Agents 与 Pipeline Failure

### Q124.1 — 基础定义

**问题：** 解释 Jenkins controller/agent 架构，以及 Pipeline 的作用。

**参考答案：**

controller 负责调度、保存 job/pipeline metadata 和协调 agents；agents 执行 build/deploy steps；Pipeline-as-code 定义 stages、conditions、parallelism、credentials 和 recovery。

### Q124.2 — 内部机制

**问题：** long-lived Jenkins agents 有哪些 reliability/security risks？

**参考答案：**

长期 agent 会积累 tools、credentials、cache、workspace state、vulnerabilities 和 cross-job contamination；恶意 job 还可能持久化。ephemeral agent 能显著降低这类风险。

### Q124.3 — 生产场景

**问题：** agent 在 deployment 中途挂掉，如何判断是否可以 safe retry？

**参考答案：**

检查哪些 side effects 已完成：deployment logs、cloud state、artifact/version markers、idempotency keys。基于真实状态 resume 或 rollback，不能把“stage failed”理解成“什么都没改”。

### Q124.4 — 故障排查

**问题：** Jenkins queue 越来越长，但部分 agents idle。怎么调查？

**参考答案：**

检查 labels、executors、node online state、resource constraints、throttling/lock plugins、stuck executors，以及 queue job 是否要求 idle agent 不具备的 label。

### Q124.5 — Senior Trade-off / Edge Case

**问题：** 如何逐步现代化脆弱 Jenkins 系统，而不是 big-bang migration？

**参考答案：**

先把 pipeline 变成 versioned/declarative，隔离 credentials，再引入 ephemeral agents 和 immutable artifacts；选代表性 jobs 分批迁移、并行验证，并保留 rollback。

---

## Q125 — Artifact Provenance、Reproducible Build 与 Deployment Race

### Q125.1 — 基础定义

**问题：** 什么叫 build once, promote the same artifact？为什么重要？

**参考答案：**

从明确 source revision 只 build/package 一次，赋予 immutable digest/version，然后把同一 artifact 在环境间 promotion。每环境重新 build 会制造 drift，削弱审计。

### Q125.2 — 内部机制

**问题：** artifact provenance 是什么？什么让 build 更 reproducible？

**参考答案：**

provenance 记录谁/什么 builder、source revision、dependencies、inputs 等；reproducibility 表示相同声明 inputs 可生成等价输出。hermetic/pinned build 更容易达到。

### Q125.3 — 生产场景

**问题：** staging 与 production 都从同一个 Git tag rebuild，但得到不同 binaries。原因可能是什么？

**参考答案：**

mutable dependencies、timestamps、环境-specific build flags、unpinned toolchain、外部 downloads、dirty workspace、不同 base image 都会改变结果。环境差异应由 runtime config，而不是重新编译决定。

### Q125.4 — 故障排查

**问题：** version label 显示 commit A，但 binary 行为像 commit B。如何调查？

**参考答案：**

核对 registry/runtime digest、deployment manifest、build attestation、image layers、binary 内嵌 Git SHA 和 pipeline run ID。不能只相信 human-readable tag。

### Q125.5 — Senior Trade-off / Edge Case

**问题：** artifact promotion 到 production 前应有哪些 supply-chain/release controls？

**参考答案：**

要求 immutable digest、适当的 signing/attestation/provenance、dependency/vulnerability checks、build/deploy authority separation、production approval，以及 post-deploy 对 expected digest 的验证。

---

# 17 — Security / PCI / Software Supply Chain

## Q126 — PCI DSS Scope 与 Cardholder Data Boundary

### Q126.1 — 基础定义

**问题：** 高层来看，哪些因素决定一个系统是否进入 PCI DSS scope？

**参考答案：**

存储、处理或传输 cardholder data 的系统会进入 scope；能够影响 cardholder-data environment 安全的系统也可能进入 scope。最终边界应由组织的 PCI/QSA 流程正式确认。

### Q126.2 — 内部机制

**问题：** PAN、cardholder data、sensitive authentication data 和 payment token 有什么区别？

**参考答案：**

PAN 是 primary account number，是 cardholder data 核心；sensitive authentication data 包括完整 track data、CVV/CVC、PIN 等，处理要求更严格。token 若不能在 tokenization system 外还原/当 PAN 使用，可显著降低暴露。

### Q126.3 — 生产场景

**问题：** 你的应用从不存 PAN，但可以影响处理 PAN 的系统安全。它是否仍可能 in scope？

**参考答案：**

可以。identity、logging、CI/CD、network controls 或可修改 CDE 软件的系统都可能影响 CDE 安全。segmentation 只有真实且经过验证才真正缩小 scope。

### Q126.4 — 故障排查

**问题：** 在 application logs 中发现敏感 payment data，立即优先做什么？

**参考答案：**

先阻止继续泄露，限制相关 logs 访问，保留 evidence，通知 security/incident process，确认数据类型、暴露时间窗和访问范围，并按批准流程处理 rotation、retention/deletion 和合规要求。

### Q126.5 — Senior Trade-off / Edge Case

**问题：** 如何通过 architecture 缩小 PCI scope，同时不牺牲 reliability 与 observability？

**参考答案：**

使用强 segmentation、tokenization、窄权限 identities、带 redaction 的独立 logging pipeline、immutable audit trail 和受控 deployment boundary。可观测性应记录业务 identifier/outcome，而不是复制 payment secrets。

---

## Q127 — Authentication、Authorization、IAM 与 Least Privilege

### Q127.1 — 基础定义

**问题：** authentication 与 authorization 有什么区别？

**参考答案：**

authentication 证明 principal 是谁；authorization 决定这个已认证 principal 可以在什么条件下对哪些资源执行什么动作。

### Q127.2 — 内部机制

**问题：** RBAC、ABAC、service identity 和 least privilege 如何关联？

**参考答案：**

RBAC 按角色授权，ABAC 按属性/tag/context，service identity 用于 workload 身份认证，而 least privilege 要求每个 identity 只拥有完成任务所需最小 action/resource/time 权限。

### Q127.3 — 生产场景

**问题：** 应用 role 因历史 IAM 问题直接用了 `AdministratorAccess`。你会怎么处理？

**参考答案：**

利用 CloudTrail/access logs 找实际所需操作，拆分 deploy/runtime/break-glass roles，增加 conditions/boundaries，并在验证后移除 broad admin。不能用永久 wildcard 来“解决” authorization design。

### Q127.4 — 故障排查

**问题：** 如何调查跨 CI、cloud IAM、Kubernetes 的 privilege escalation path？

**参考答案：**

画出所有 trust edges：谁能改 workflow、assume role、创建 token、修改 cluster RBAC、impersonate service account；检查 trust policies、OIDC claims、CI permissions、K8s bindings 和 audit logs 的 transitive path。

### Q127.5 — Senior Trade-off / Edge Case

**问题：** 事故中如何兼顾 least privilege 与 operational usability？

**参考答案：**

正常身份保持 least privilege；为事故准备 audited、time-bounded break-glass elevation、强认证和事后 review。事故速度来自预先设计 emergency access，不是常驻过度权限。

---

## Q128 — Secrets、KMS、Encryption 与 Rotation

### Q128.1 — 基础定义

**问题：** encryption at rest、encryption in transit 和 secret management 有什么区别？

**参考答案：**

at-rest encryption 保护存储数据，in-transit encryption 保护传输中的数据，secret management 管理 credential/key 从创建、存储、访问、rotation 到 revocation 的整个生命周期。

### Q128.2 — 内部机制

**问题：** 高层解释 KMS-style envelope encryption。

**参考答案：**

先用 data-encryption key 加密 payload，再用 KMS-managed key 加密这个 data key；encrypted data key 可和 ciphertext 一起存储，而真正 unwrap 由 KMS policy/audit 控制。

### Q128.3 — 生产场景

**问题：** 一个 database password 被 commit 到 Git 且已经用于 production。下一步？

**参考答案：**

视为 compromised：立即 revoke/rotate，从 active code 移除，检查 Git history、CI artifacts、logs 和谁可访问，然后按 incident process 评估。仅 rewrite Git history 不能让旧 credential 重新安全。

### Q128.4 — 故障排查

**问题：** 如何给数千 application instances rotate secret 而不 outage？

**参考答案：**

支持 overlap：发布 new version secret，逐步让 clients 使用并验证 adoption，再 revoke old secret。最好支持 dynamic reload 或 dual-validity window，并监控 auth failures。

### Q128.5 — Senior Trade-off / Edge Case

**问题：** workload 什么时候应该用 long-lived secret，什么时候优先 short-lived identity credentials？

**参考答案：**

平台支持时优先 short-lived federated/workload identity credentials。static long-lived secrets 在 rotation、泄露、复制和撤销方面更难管理，应是例外。

---

## Q129 — Network Security、TLS、mTLS 与 Segmentation

### Q129.1 — 基础定义

**问题：** TLS 提供哪些 security properties？它本身不能解决什么？

**参考答案：**

TLS 提供传输 confidentiality、integrity 和基于证书/trust model 的 endpoint authentication。它不会自动授权业务动作、修复 compromised endpoint，也保护不了解密后的数据。

### Q129.2 — 内部机制

**问题：** mTLS 比普通 server-authenticated TLS 多了什么 guarantee？

**参考答案：**

mTLS 让双方都提供证书，因此 server 可获得 cryptographic client/workload identity；但 authorization 仍需要基于这个 identity 的 policy。

### Q129.3 — 生产场景

**问题：** 团队认为 private network 足够安全，因此建议 service-to-service 明文。你怎么评估？

**参考答案：**

private routing 只能减少暴露，无法防 compromised workload、misrouting、insider 或 lateral movement。应基于数据敏感度、threat model、平台成本决定是否需要 service identity/encryption。

### Q129.4 — 故障排查

**问题：** certificate rotation 导致 mTLS outage，怎么诊断？

**参考答案：**

检查 certificate expiry/not-before、trust bundle、issuer/chain、SAN identity、clock skew、old/new CA overlap、proxy config 和 handshake errors。rotation 应提供 trust overlap，不应一次性全球原子替换。

### Q129.5 — Senior Trade-off / Edge Case

**问题：** network segmentation 如何减少 blast radius，同时避免变得不可运维？

**参考答案：**

按 trust/data boundary 和最小 service need 分段，policy as code，尽量 identity/service-oriented，监控 denies，持续 connectivity test，并提供受控 break-glass，而不是永久 broad exception。

---

## Q130 — Software Supply Chain 与 Vulnerability Management

### Q130.1 — 基础定义

**问题：** CI/CD pipeline 的主要 software supply-chain risks 有哪些？

**参考答案：**

风险包括 source account compromise、恶意 dependencies、poisoned runners、mutable artifacts/tags、stolen signing/deploy credentials、dependency confusion、vulnerable build tools 和 tampered registries。

### Q130.2 — 内部机制

**问题：** 按 version pin dependency 与按 immutable digest/commit pin 有什么区别？

**参考答案：**

semantic version/tag 有时可被重新发布或解析到不同内容，而 digest/commit 指向精确 immutable content。pinning 提高 reproducibility，但还要有正常 update process，避免永远不升级。

### Q130.3 — 生产场景

**问题：** 数百 services 使用的 base image 出现 critical vulnerability，怎么响应？

**参考答案：**

先评估 exploitability/exposure，用 digest/SBOM 找受影响 running artifacts，优先 internet-facing/高权限 services，从 patched base 重建、canary、rollout，并验证旧 vulnerable instances 已清除。

### Q130.4 — 故障排查

**问题：** 如何判断 vulnerability 需要 emergency patch 还是 normal remediation？

**参考答案：**

结合 severity、reachable code、internet exposure、privilege、available exploit、compensating controls、asset criticality 和 patch risk，不能只看 CVSS。

### Q130.5 — Senior Trade-off / Edge Case

**问题：** 哪些 controls 可以降低恶意/被攻陷 dependency 进入 production 的风险？

**参考答案：**

source repo protection、review、least-privilege CI、pinned/verified dependencies、trusted registry、SBOM/provenance、适当 signing/attestation、dependency scanning、ephemeral isolated builders 和受控 promotion。

---

## Q131 — Audit Logs、PII、Patching 与 Security Evidence

### Q131.1 — 基础定义

**问题：** audit log 与普通 application debug log 有什么区别？

**参考答案：**

audit log 记录 security/business-relevant action，包括 actor、action、target、result 和可信时间/context，并应防未经授权修改；debug log 主要服务 troubleshooting。

### Q131.2 — 内部机制

**问题：** PII 和敏感字段在 observability data 中应如何处理？

**参考答案：**

只采集必要最小数据，分类字段，在 source 端 redact/tokenize，限制访问，加密传输/存储，设置合理 retention，并尽量让 secrets/PAN 从一开始就不进入 logs。

### Q131.3 — 生产场景

**问题：** production privileged change 没有可靠 audit trail，为什么既是 compliance problem，也是 operational problem？

**参考答案：**

没有可信 actor/timeline，就无法判断 blast radius、重建 root cause、验证 rollback，也无法区分 human error 与 compromise，因此 incident response 和 accountability 都受损。

### Q131.4 — 故障排查

**问题：** 如何低风险 patch security-sensitive production fleet？

**参考答案：**

盘点受影响版本，先 representative env/canary，保留安全 rollback，监控 SLO/security signals，自动分批 rollout，并按 exposure 优先级处理。emergency patch 仍需要 artifact integrity 和 change tracking。

### Q131.5 — Senior Trade-off / Edge Case

**问题：** security incident 中 evidence preservation 与普通 SRE recovery 目标什么时候会冲突？

**参考答案：**

快速 restart/rebuild 可能破坏 volatile evidence。应与 security/forensics 协调先抓必要 snapshots/logs 或隔离 compromised systems，再使用 known-good artifacts/credentials 恢复。

---

# 18 — Document Database / NoSQL

## Q132 — Document Database Data Modelling、Index 与 Query

### Q132.1 — 基础定义

**问题：** document-oriented data modelling 与 normalized relational modelling 有什么区别？

**参考答案：**

document DB 通常围绕 aggregate-shaped record 存储，并倾向 denormalization，把经常一起读取的数据放在一起；relational model 更强调 normalized tables、joins 和跨行 constraints。

### Q132.2 — 内部机制

**问题：** 什么时候应该 embed related data，什么时候单独 reference？

**参考答案：**

相同 lifecycle、size 有界、经常一起读时适合 embed；many-to-many、独立更新、无界增长或广泛共享时更适合 reference。

### Q132.3 — 生产场景

**问题：** collection 增长后 document DB query 越来越慢。检查什么？

**参考答案：**

检查 filter/sort/query shape、execution plan、index coverage/order、selectivity、document size、working set/cache、partition/shard targeting，以及是否扫描大量 documents。

### Q132.4 — 故障排查

**问题：** 为什么 index 可以让某个 query 更快，却让整体 workload 变差？

**参考答案：**

index 消耗 storage/memory，并让每次 insert/update/delete 产生 write amplification。太多或低 selectivity indexes 会伤 write throughput 和 cache efficiency。

### Q132.5 — Senior Trade-off / Edge Case

**问题：** 新服务什么时候选择 document database，而不是 PostgreSQL？

**参考答案：**

基于 access patterns、aggregate flexibility、schema evolution、partition/scaling、consistency/transaction needs、operational maturity 和团队经验选择，而不是因为 JSON 看起来方便。

---

## Q133 — Document DB Replication、Consistency 与 Failover

### Q133.1 — 基础定义

**问题：** replica set 或 replicated document DB cluster 主要想提供什么能力？

**参考答案：**

replication 通过多份数据提供 redundancy、部分 read scaling 和 failover。常见模式是 leader/primary 协调 writes，followers 按顺序复制 operation/log。

### Q133.2 — 内部机制

**问题：** primary/leader reads/writes、replicas、replication lag 和 consistency 怎么互相影响？

**参考答案：**

更强 write acknowledgement 等待更多 durable/replicated confirmation，latency 更高；从 replica 读可减轻 primary load，但 lag 时可能 stale。具体语义因数据库实现不同。

### Q133.3 — 生产场景

**问题：** failover 后 client 暂时看到 stale data 或 write errors，为什么？

**参考答案：**

leader election 需要时间，in-flight writes 可能处于 unknown outcome，client topology cache 可能过时，replica read 仍有 lag，弱 durability write 甚至可能在某些 failover 中丢失。

### Q133.4 — 故障排查

**问题：** replication lag 持续增长怎么调查？

**参考答案：**

看 network latency、replica CPU/disk I/O、replication/oplog queue、long-running ops、write burst、checkpoint/compaction pressure，并判断是单 replica 还是全体 lag。

### Q133.5 — Senior Trade-off / Edge Case

**问题：** payment-adjacent critical state 与 non-critical metadata 应如何选择 read/write consistency？

**参考答案：**

correctness-critical state 应优先 durable acknowledged write 和足够强 read，即使牺牲 latency；cache-like metadata 可接受 bounded staleness。选择必须由 business invariant 决定。

---

## Q134 — Document DB Sharding、Hot Partition、Schema Evolution 与 Backup

### Q134.1 — 基础定义

**问题：** 为什么 document DB 要 sharding？什么是好 shard/partition key？

**参考答案：**

sharding 用来把 data/load 分散到多个 nodes。好的 key 应有足够 cardinality、均匀分布 reads/writes、支持常见 query routing，并避免 monotonically 把新流量集中到单 shard。

### Q134.2 — 内部机制

**问题：** 什么是 hot partition？错误 shard key 如何制造它？

**参考答案：**

hot partition 是某个 partition 承担不成比例的 traffic/data，导致 cluster average 看起来正常但单 shard 饱和。单大 tenant 或 sequential timestamp 都可能造成热点。

### Q134.3 — 生产场景

**问题：** traffic 增长后只有一个 shard saturation，其他都 idle。怎么诊断？

**参考答案：**

按 shard key/tenant/range 拆 traffic，检查 router targeting、shard CPU/disk/queue、document size 和 write patterns，再看 key distribution。增加 idle shards 并不能解决 concentrated key。

### Q134.4 — 故障排查

**问题：** old/new application versions 同时运行时，document schema 如何演进？

**参考答案：**

使用 tolerant readers、additive fields、合理 defaults，必要时 version marker，并分阶段 backfill。new writers 不应立即写 old readers 无法理解的数据，除非 rollout 顺序严格保证。

### Q134.5 — Senior Trade-off / Edge Case

**问题：** 怎样测试才能真正信任 document DB backup/restore？

**参考答案：**

必须测试 point-in-time correctness、encryption key availability、restore time、indexes、sharded topology metadata、application compatibility、RPO/RTO，以及真正 end-to-end recovery，不是只确认 backup 文件存在。

---

# 19 — Architecture / Technical Leadership

## Q135 — End-to-End System Architecture Reasoning

### Q135.1 — 基础定义

**问题：** review production architecture 时，除了 components 能连通，还应该看哪些维度？

**参考答案：**

应看 user journeys、SLO、failure domains、data correctness、capacity、dependencies、security boundaries、observability、deployment/recovery、operability、cost 和 ownership。只有 connectivity 的图不算完整 architecture review。

### Q135.2 — 内部机制

**问题：** 如何发现 distributed cloud system 中隐藏的 single points of failure？

**参考答案：**

沿 critical user path 逐个问 node/AZ/region/credential/queue/DNS/DB/control-plane/third party 变慢或不可用会怎样，而不只测试完全 down。shared state/control dependencies 常是隐藏 SPOF。

### Q135.3 — 生产场景

**问题：** compute layer 很 HA，但依赖单 region DB 和一个 external API。你会怎么挑战这个设计？

**参考答案：**

compute redundancy 无法把 end-to-end availability 提高到 mandatory single dependency 之上。量化 dependency SLO/failure modes，再考虑 failover、cache/degradation、async decoupling，或诚实调整 service SLO。

### Q135.4 — 故障排查

**问题：** 上线前如何验证 architecture 的 failure assumptions？

**参考答案：**

用 load test、dependency failure injection、AZ/node loss game、backup restore、credential/cert rotation、rollback test 和 synthetic journey 去验证，而不是靠 diagram 假设。

### Q135.5 — Senior Trade-off / Edge Case

**问题：** 如何平衡 availability、consistency、latency、security、cost 和 operational complexity？

**参考答案：**

先从 business/user invariants 和 SLO 出发，选择满足目标的最简单设计。每增加 replica/quorum/cache/region/encryption layer 都会带来新成本和 failure modes，复杂度必须买到可测量风险下降。

---

## Q136 — ADR、Architecture Trade-off、Cost 与 Business Impact

### Q136.1 — 基础定义

**问题：** 一个好的 Architecture Decision Record 应包含什么？

**参考答案：**

好的 ADR 应记录 context、decision、alternatives、关键 constraints、trade-offs、consequences、owner/date，以及什么 evidence 会触发重新评估。它保存 reasoning，而不是复制巨型 design doc。

### Q136.2 — 内部机制

**问题：** decision reversibility 应如何影响架构决策过程？

**参考答案：**

可逆 decision 可以更快实验并依靠 guardrails；难逆 decision，如 data model、public API、regional topology，需要更多 evidence、migration plan 和 stakeholder review。

### Q136.3 — 生产场景

**问题：** 两个设计都满足 SLO，其中一个贵一倍但更容易运维。怎么比较？

**参考答案：**

比较 total cost of ownership：cloud spend、engineer/on-call time、incident probability/impact、delivery speed、vendor risk 和 future change cost。最便宜 infra bill 不一定是最低总成本。

### Q136.4 — 故障排查

**问题：** 如何向非技术 stakeholders 解释 reliability investment？

**参考答案：**

把技术风险转成 customer impact、revenue/contract risk、error-budget burn、recovery time、engineer interruption 和 probability-weighted loss，并说明投资能买到什么 outcome、如何衡量。

### Q136.5 — Senior Trade-off / Edge Case

**问题：** 什么时候 Senior SRE 应接受技术上不那么优雅的设计？

**参考答案：**

当它更符合 business timing、team capability、migration risk、cost 或 reversibility，同时仍满足安全/SLO 底线时，应接受。工程质量不是最大化抽象或新技术。

---

## Q137 — Mentoring、Code Review、Influence 与 Technical Leadership

### Q137.1 — 基础定义

**问题：** Senior SRE 做 code/infra review 的目标，除了找错误还有什么？

**参考答案：**

除了保护 correctness、reliability、security、maintainability，还应传递 context 和 reasoning，让团队下一次 change 自己做得更好，而不是只 block 当前 patch。

### Q137.2 — 内部机制

**问题：** 一个 engineer 反复做高风险 production changes，你如何 mentor？

**参考答案：**

用具体 incident/change 做 evidence，先理解为什么产生风险，再教 precondition、blast radius、recovery thinking，pair 做一次更安全 change，并建立 guardrails/checklist。只有持续 unsafe behaviour 才升级。

### Q137.3 — 生产场景

**问题：** 你非常不同意 Tech Lead 的 architecture，但项目归他负责。怎么办？

**参考答案：**

先确认共同目标/constraints，把 disagreement 和 evidence 写清楚，提出 experiment/failure analysis，并邀请相关 reviewers。一旦责任人基于充分信息做出决定，应支持执行，除非触及严重 safety/security boundary。

### Q137.4 — 故障排查

**问题：** 如何提高 engineering standards，又不把自己变成 bottleneck/gatekeeper？

**参考答案：**

把可重复 policy 自动化，提供 paved-road modules/templates 和例子，通过 coaching 提高团队；只有真正高风险 decision 才保留人工 gate，不应让每个 change 都依赖自己。

### Q137.5 — Senior Trade-off / Edge Case

**问题：** 面试另一个 Senior SRE 时你会重点看什么？

**参考答案：**

看 structured troubleshooting、对自称熟悉技术的深度、production judgement、uncertainty 下表达、coding/automation、reliability/security trade-offs、学习能力和团队 influence，而不是 trivia 数量。

---

# 20 — API Semantics / GraphQL / Disaster Recovery

## Q138 — HTTP API Semantics：Caching、Pagination、Rate Limit、Idempotency

### Q138.1 — 基础定义

**问题：** 可靠 API operation 中，哪些 HTTP method/status semantics 最重要？

**参考答案：**

需要理解 safe/idempotent methods、2xx/4xx/5xx、redirect、409/412 conflict、429 rate limit 和 202 async workflow。status code 应保留 operational meaning，不能所有失败都变成 500。

### Q138.2 — 内部机制

**问题：** cache headers、ETag、conditional request 和 pagination 如何影响 correctness 与 operability？

**参考答案：**

Cache-Control 管 freshness/revalidation；ETag 支持 conditional GET/update 和 optimistic concurrency；cursor pagination 提供较稳定 continuation model。这些都会直接影响 load 和 correctness。

### Q138.3 — 生产场景

**问题：** offset pagination 在数据持续变化时出现 duplicate/missing records，为什么？

**参考答案：**

offset 代表动态 ordered result 中的位置，前面 insert/delete 后后续 offset 会位移，导致 skip/duplicate。基于 immutable ordering key 的 cursor/keyset pagination 通常更安全。

### Q138.4 — 故障排查

**问题：** 如何设计 rate limiting 与 client behaviour，避免 overload 演化成 retry storm？

**参考答案：**

server 侧用 token bucket/leaky bucket，429 返回 `Retry-After` 等 guidance；client 使用 bounded backoff+jitter，并区分 per-user/per-tenant/global limits，保护真正 downstream capacity。

### Q138.5 — Senior Trade-off / Edge Case

**问题：** create/update API 如何做到对 retry 和 concurrent clients 安全？

**参考答案：**

create 用 idempotency key，update 用 ETag/version/If-Match 或 DB CAS，配合 atomic uniqueness constraint 和明确 conflict response，让 retry 收敛到同一 business outcome。

---

## Q139 — GraphQL Resolver、N+1、Complexity 与 Security

### Q139.1 — 基础定义

**问题：** GraphQL execution 在运维上与固定 REST endpoint 有什么不同？

**参考答案：**

client 可选择 query shape，因此每个 request 的 server cost 变化很大，resolver 动态组合。一个 endpoint 实际代表许多 logical operations，所以 query complexity、authorization、caching 和 per-field latency 都很重要。

### Q139.2 — 内部机制

**问题：** 什么是 N+1 problem？batching/DataLoader-style pattern 如何解决？

**参考答案：**

parent resolver 可能对每个 item 再发一次 child query，形成 N+1 calls；batching 把多个 keys 合并成少量 backend requests，per-request cache 避免重复 fetch。

### Q139.3 — 生产场景

**问题：** 单个 GraphQL query 导致数千 DB calls 和高 CPU，incident 中怎么先止血？

**参考答案：**

先限制 depth/complexity、rate limit、timeout，必要时临时 disable/cap pathological field，保护 DB pool，并看 slow resolver traces；稳定后再修 batching/index/query planning。

### Q139.4 — 故障排查

**问题：** 如何让 GraphQL 足够 observable，同时避免把 raw query text 作为高 cardinality metric label？

**参考答案：**

记录 operation name/hash、bounded complexity buckets、resolver latency/error、backend call count 和 sampled traces。完整 query text 只在受控 logs/traces 中按需保存，不做 metric label。

### Q139.5 — Senior Trade-off / Edge Case

**问题：** public GraphQL endpoint 应加哪些 security/reliability controls？

**参考答案：**

加入 depth/complexity/result-size limits、resolver/domain auth、rate limit、persisted queries（适用时）、introspection policy、input validation、timeouts、batching 和 query-cost observability。

---

## Q140 — Disaster Recovery：Backup、Restore、RTO、RPO 与 Failover Drill

### Q140.1 — 基础定义

**问题：** RTO 和 RPO 是什么？区别在哪里？

**参考答案：**

RTO 是 disaster 后恢复到可接受 service 的目标时间；RPO 是最多允许丢失多少时间/transactions 的数据。两者决定 replication、backup frequency、architecture 和 staffing。

### Q140.2 — 内部机制

**问题：** 为什么“有 backup”不等于“有 disaster recovery capability”？

**参考答案：**

DR 还必须恢复 data、infrastructure、identity、secrets/keys、dependencies、DNS/traffic 和 application correctness，并在 RTO/RPO 内完成。未演练 backup 只能说明某处可能有数据。

### Q140.3 — 生产场景

**问题：** database backup 还在，但 encryption key 和 IAM config 都丢了，意味着什么？

**参考答案：**

backup 可能实际上不可用。KMS keys、credentials、IaC、certificates 和 account access 都是 recovery dependencies，必须有独立可恢复方案。

### Q140.4 — 故障排查

**问题：** 如何做 realistic DR exercise，同时避免给 production 带来不可接受风险？

**参考答案：**

定义 scenario/success criteria，在独立 environment/account/region restore，验证 data 和 synthetic user journey，测量 elapsed time/data loss，演练 traffic decision，并记录 gaps，避免修改当前 production state。

### Q140.5 — Senior Trade-off / Edge Case

**问题：** 如何选择 active-active、warm standby、pilot light、backup/restore 或更简单 single-region recovery？

**参考答案：**

选择能满足业务 RTO/RPO 的最低复杂度模式。active-active 可减少部分恢复时间，但 consistency 和运维复杂度极高；backup/restore 成本低但恢复慢。目标必须足以证明额外架构复杂度合理。

---

# 训练标准

- **Bronze：** `.1`、`.2` 不看资料可以准确回答。
- **Silver：** 可以回答到 `.3`，并给出真实 production example。
- **Gold：** 可以回答到 `.4`，使用 hypothesis-driven troubleshooting。
- **Pushpay-ready：** 五层全部可以回答，并能在 follow-up 下解释 trade-off、failure mode 和 alternative design。

故障题建议始终使用：

```text
Impact → Scope → Timeline → Hypotheses → Evidence → Mitigation → Root Cause → Prevention
```

不要随机报十几个命令。每个检查都应该回答一个明确问题，并改变你对某个 hypothesis 的 confidence。

# Self-rating 提醒

如果面试官让你按 1–5 给技术栈自评，可以按下面理解：

```text
1/5 — 只知道概念
2/5 — 有基础接触，复杂工作需要帮助
3/5 — 可以独立完成日常生产工作
4/5 — 有深入生产经验，可以 troubleshooting 并指导别人
5/5 — 接近 Subject-Matter Expert，愿意接受 internals / edge cases 级别追问
```

把 **5/5** 理解成：面试官可以继续问 implementation internals、failure modes、production troubleshooting 和 design trade-offs。