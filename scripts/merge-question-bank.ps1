param(
    [string]$ChinesePath = "AWS_AIP-C01_中文版.md",
    [string]$EnglishPath = "src/main/resources/question-bank/AWS_AIP-C01.md",
    [string]$BilingualPath = "AWS_AIP-C01_中文版.md",
    [string]$ResourcePath = "src/main/resources/question-bank/AWS_AIP-C01_bilingual.md"
)

$ErrorActionPreference = "Stop"

function Get-QuestionBlocks {
    param([string]$Path, [string]$HeadingPattern)

    $lines = @(Get-Content -LiteralPath $Path -Encoding UTF8)
    $starts = @(0..($lines.Count - 1) | Where-Object { $lines[$_] -match $HeadingPattern })
    $result = @{}

    for ($blockIndex = 0; $blockIndex -lt $starts.Count; $blockIndex++) {
        $start = $starts[$blockIndex]
        $end = if ($blockIndex + 1 -lt $starts.Count) { $starts[$blockIndex + 1] } else { $lines.Count }
        $heading = $lines[$start]
        $number = [int]([regex]::Match($heading, '\d+').Value)
        $questionLines = [System.Collections.Generic.List[string]]::new()
        $options = [ordered]@{}
        $currentLabel = $null
        $inVotes = $false
        $answer = ""
        $votes = [System.Collections.Generic.List[string]]::new()

        for ($lineIndex = $start + 1; $lineIndex -lt $end; $lineIndex++) {
            $raw = $lines[$lineIndex]
            $line = $raw.Trim()
            if ($line -match '(?i)Correct Answer|正确答案') {
                $answerMatch = [regex]::Match($line, '(?i)(?:Correct Answer|正确答案).*?`?\s*([A-F]+)\s*`?\s*$')
                if ($answerMatch.Success) { $answer = $answerMatch.Groups[1].Value.ToUpperInvariant() }
                $currentLabel = $null
                continue
            }
            if ($line -match '(?i)Community vote distribution|社区投票分布') {
                $inVotes = $true
                $currentLabel = $null
                continue
            }
            if ($inVotes) {
                $voteMatch = [regex]::Match($line, '^\s*-?\s*([A-F]{1,6})\s*[\(（\[]\s*(\d{1,3})\s*%\s*[\)）\]]\s*$')
                if ($voteMatch.Success) { $votes.Add("$($voteMatch.Groups[1].Value.ToUpperInvariant()) ($($voteMatch.Groups[2].Value)%)") }
                continue
            }
            $optionMatch = [regex]::Match($raw, '^\s*-\s+\*\*([A-F])\.\*\*\s*(.*?)\s*$')
            if ($optionMatch.Success) {
                $label = $optionMatch.Groups[1].Value.ToUpperInvariant()
                $text = $optionMatch.Groups[2].Value.Trim()
                $mostVoted = $text -match '(?i)Most Voted|最高票|得票最高'
                $text = [regex]::Replace($text, '(?i)\s*\*{0,2}\s*(?:\(Most Voted\)|（最高票）|（得票最高）|\(最高票\)|\(得票最高\))\s*\*{0,2}', '').Trim()
                $options[$label] = [pscustomobject]@{ Text = $text; MostVoted = $mostVoted }
                $currentLabel = $label
                continue
            }
            if ($currentLabel -and -not [string]::IsNullOrWhiteSpace($line) -and $line -notmatch '^---+$') {
                $options[$currentLabel].Text = ($options[$currentLabel].Text + ' ' + $line).Trim()
            } elseif (-not [string]::IsNullOrWhiteSpace($line) -and $line -notmatch '^[>#]') {
                $questionLines.Add($line)
            }
        }

        $result[$number] = [pscustomobject]@{
            Number = $number
            Text = ($questionLines -join "`n").Trim()
            Options = $options
            Answer = $answer
            Votes = $votes
        }
    }
    return $result
}

function Get-BilingualQuestionBlocks {
    param([string]$Path)

    $lines = @(Get-Content -LiteralPath $Path -Encoding UTF8)
    $starts = @(0..($lines.Count - 1) | Where-Object { $lines[$_] -match '^##\s+Question\s+\d+' })
    $result = @{}

    for ($blockIndex = 0; $blockIndex -lt $starts.Count; $blockIndex++) {
        $start = $starts[$blockIndex]
        $end = if ($blockIndex + 1 -lt $starts.Count) { $starts[$blockIndex + 1] } else { $lines.Count }
        $number = [int]([regex]::Match($lines[$start], '\d+').Value)
        $textByLanguage = @{ zh = [System.Collections.Generic.List[string]]::new(); en = [System.Collections.Generic.List[string]]::new() }
        $optionsByLanguage = @{ zh = [ordered]@{}; en = [ordered]@{} }
        $language = $null
        $currentLabel = $null
        $inVotes = $false
        $answer = ""
        $votes = [System.Collections.Generic.List[string]]::new()

        for ($lineIndex = $start + 1; $lineIndex -lt $end; $lineIndex++) {
            $raw = $lines[$lineIndex]
            $line = $raw.Trim()
            if ($line -match '^###\s+中文\s*$') { $language = 'zh'; $currentLabel = $null; continue }
            if ($line -match '^###\s+English\s*$') { $language = 'en'; $currentLabel = $null; continue }
            if ($line -match '(?i)Correct Answer|正确答案') {
                $answerMatch = [regex]::Match($line, '(?i)(?:Correct Answer|正确答案).*?`?\s*([A-F]+)\s*`?\s*$')
                if ($answerMatch.Success) { $answer = $answerMatch.Groups[1].Value.ToUpperInvariant() }
                $currentLabel = $null
                continue
            }
            if ($line -match '(?i)Community vote distribution|社区投票分布') { $inVotes = $true; $currentLabel = $null; continue }
            if ($inVotes) {
                $voteMatch = [regex]::Match($line, '^\s*-?\s*([A-F]{1,6})\s*[\(（\[]\s*(\d{1,3})\s*%\s*[\)）\]]\s*$')
                if ($voteMatch.Success) { $votes.Add("$($voteMatch.Groups[1].Value.ToUpperInvariant()) ($($voteMatch.Groups[2].Value)%)") }
                continue
            }
            if (-not $language) { continue }
            $optionMatch = [regex]::Match($raw, '^\s*-\s+\*\*([A-F])\.\*\*\s*(.*?)\s*$')
            if ($optionMatch.Success) {
                $label = $optionMatch.Groups[1].Value.ToUpperInvariant()
                $text = $optionMatch.Groups[2].Value.Trim()
                $mostVoted = $text -match '(?i)Most Voted|最高票|得票最高'
                $text = [regex]::Replace($text, '(?i)\s*\*{0,2}\s*(?:\(Most Voted\)|（最高票）|（得票最高）|\(得票最高\))\s*\*{0,2}', '').Trim()
                $optionsByLanguage[$language][$label] = [pscustomobject]@{ Text = $text; MostVoted = $mostVoted }
                $currentLabel = $label
                continue
            }
            if ($currentLabel -and -not [string]::IsNullOrWhiteSpace($line) -and $line -notmatch '^---+$') {
                $optionsByLanguage[$language][$currentLabel].Text = ($optionsByLanguage[$language][$currentLabel].Text + ' ' + $line).Trim()
            } elseif (-not [string]::IsNullOrWhiteSpace($line) -and $line -notmatch '^[>#]') {
                $textByLanguage[$language].Add($line)
            }
        }

        $result[$number] = [pscustomobject]@{
            Number = $number
            TextZh = ($textByLanguage.zh -join "`n").Trim()
            TextEn = ($textByLanguage.en -join "`n").Trim()
            OptionsZh = $optionsByLanguage.zh
            OptionsEn = $optionsByLanguage.en
            Answer = $answer
            Votes = $votes
        }
    }
    return $result
}

$english = Get-QuestionBlocks -Path $EnglishPath -HeadingPattern '^##\s+Question\s+\d+'
$inputLines = @(Get-Content -LiteralPath $ChinesePath -Encoding UTF8)
if ($inputLines | Where-Object { $_ -match '^##\s+题目\s+\d+' }) {
    $chinese = Get-QuestionBlocks -Path $ChinesePath -HeadingPattern '^##\s+题目\s+\d+'
    if ($chinese.Count -ne $english.Count -or $chinese.Count -ne 117) {
        throw "Question count mismatch: Chinese=$($chinese.Count), English=$($english.Count)"
    }
    $pairs = foreach ($number in ($english.Keys | Sort-Object {[int]$_})) {
        [pscustomobject]@{ Number = $number; Chinese = $chinese[$number]; English = $english[$number] }
    }
} else {
    $existing = Get-BilingualQuestionBlocks -Path $ChinesePath
    if ($existing.Count -ne $english.Count -or $existing.Count -ne 117) {
        throw "Bilingual question count mismatch: Existing=$($existing.Count), English=$($english.Count)"
    }
    $pairs = foreach ($number in ($english.Keys | Sort-Object {[int]$_})) {
        $current = $existing[$number]
        $source = $english[$number]
        if ($current.OptionsZh.Count -ne $source.Options.Count -or $current.OptionsEn.Count -ne $source.Options.Count) {
            throw "Question $number option count mismatch between bilingual and English source"
        }
        [pscustomobject]@{
            Number = $number
            Chinese = [pscustomobject]@{ Text = $current.TextZh; Options = $current.OptionsZh; Answer = $current.Answer }
            English = [pscustomobject]@{ Text = $current.TextEn; Options = $current.OptionsEn; Answer = $current.Answer; Votes = $source.Votes }
        }
    }
}

$output = [System.Collections.Generic.List[string]]::new()
$output.Add('# AWS Certified Generative AI Developer - Professional（AIP-C01）中英双语版')
$output.Add('')
$output.Add('> 中文与 English 题干、选项按题号配对；正确答案和社区投票数据沿用原始题库。')
$output.Add('> 语言切换由 CertForge 网页控制，Markdown 本身保留两种语言，便于离线阅读和维护。')
$output.Add('')
$output.Add('---')
$output.Add('')

foreach ($pair in $pairs) {
    $number = $pair.Number
    $cn = $pair.Chinese
    $en = $pair.English
    if ($cn.Options.Count -ne $en.Options.Count -or $cn.Answer -ne $en.Answer) {
        throw "Question $number mismatch between Chinese and English source"
    }
    $output.Add("## Question $number - Topic 1")
    $output.Add('')
    $output.Add('### 中文')
    $output.Add('')
    $output.Add($cn.Text)
    $output.Add('')
    foreach ($label in $cn.Options.Keys) {
        $suffix = if ($cn.Options[$label].MostVoted) { ' **（最高票）**' } else { '' }
        $output.Add("- **$label.** $($cn.Options[$label].Text)$suffix")
    }
    $output.Add('')
    $output.Add('### English')
    $output.Add('')
    $output.Add($en.Text)
    $output.Add('')
    foreach ($label in $en.Options.Keys) {
        $suffix = if ($en.Options[$label].MostVoted) { ' **(Most Voted)**' } else { '' }
        $output.Add("- **$label.** $($en.Options[$label].Text)$suffix")
    }
    $output.Add('')
    $output.Add("**Correct Answer / 正确答案:** ``$($en.Answer)``")
    $output.Add('')
    $output.Add('**Community vote distribution / 社区投票分布:**')
    $output.Add('')
    foreach ($vote in $en.Votes) { $output.Add("- $vote") }
    $output.Add('')
    $output.Add('---')
    $output.Add('')
}

$utf8NoBom = New-Object System.Text.UTF8Encoding($false)
[System.IO.File]::WriteAllText((Join-Path (Get-Location) $BilingualPath), ($output -join "`r`n"), $utf8NoBom)
[System.IO.File]::Copy((Join-Path (Get-Location) $BilingualPath), (Join-Path (Get-Location) $ResourcePath), $true)
Write-Output "Generated $BilingualPath and $ResourcePath with $($english.Count) bilingual questions."
