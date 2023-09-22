/*
 * Copyright (C) 2019 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.devbyteviewer.util

@Suppress("PrivatePropertyName") // It is a const (of sorts)
private val PUNCTUATION = listOf(", ", "; ", ": ", " ")

/**
 * Extension function of [String] which truncates long text with a preference for word boundaries
 * and without trailing punctuation. We first initialize our [List] of [String] variable `val words`
 * to our receiver (implied `this`) split around occurrences of the space character. We initialize
 * our variable `var added` to 0 (keeps track of how many words we add, but we do not use this so
 * it is probably just for debugging). We initialize our [Boolean] variable `var hasMore` to `false`
 * (set to `true` if there are more than our [Int] parameter [length] characters in the target
 * [String] so we can add "..." to the output to signify this), and we initialize our [StringBuilder]
 * variable `val builder` to a new instance.
 *
 * Now we loop for all the [String] `word` in `words`:
 *  - If the `length` of `builder` is greater than our parameter [length] we set `hasMore` to `true
 *  and break out of the loop.
 *  - We append `word` to `builder` followed by a space character, increment `added` and loop around
 *  for the next `word`
 *
 * We use `map` to loop through all the punctuation characters in [PUNCTUATION] and if `builder`
 * ends with the characters we remove it from the end of `builder` (Note that each punctuation
 * character in [PUNCTUATION] is followed by a space character, and a single space character is
 * included so the separating space character added after the final word is always removed as well).
 *
 * If our `hasMore` variable is `true` we add an ellipsis ("...") to `builder`, and finally we
 * return the [String] value of `builder` to the caller.
 *
 * @param length the approximate length we are to truncate our [String] to.
 * @return `this` truncated to approximately [length] characters with a preference for word
 * boundaries and without trailing punctuation.
 */
fun String.smartTruncate(length: Int): String {
    val words = split(" ")
    var added = 0
    var hasMore = false
    val builder = StringBuilder()
    for (word in words) {
        if (builder.length > length) {
            hasMore = true
            break
        }
        builder.append(word)
        builder.append(" ")
        added += 1
    }

    PUNCTUATION.map {
        if (builder.endsWith(it)) {
            builder.replace(builder.length - it.length, builder.length, "")
        }
    }

    if (hasMore) {
        builder.append("...")
    }
    return builder.toString()
}