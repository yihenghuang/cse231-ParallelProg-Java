/*******************************************************************************
 * Copyright (C) 2016-2017 Dennis Cosgrove
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/
package mapreduce.framework.warmup.wordcount.util;

import mapreduce.apps.wordcount.core.TextSection;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public enum TextSectionResource {
	QUICK_BROWN_FOX(
			"The quick brown fox jumped over the lazy dog."
	),
	GETTYSBURG_ADDRESS(
			"Four score and seven years ago our fathers brought forth on this continent, a new nation, conceived in Liberty, and dedicated to the proposition that all men are created equal.",
			"Now we are engaged in a great civil war, testing whether that nation, or any nation so conceived and so dedicated, can long endure. We are met on a great battle-field of that war. We have come to dedicate a portion of that field, as a final resting place for those who here gave their lives that that nation might live. It is altogether fitting and proper that we should do this.",
			"But, in a larger sense, we can not dedicate—we can not consecrate—we can not hallow—this ground. The brave men, living and dead, who struggled here, have consecrated it, far above our poor power to add or detract. The world will little note, nor long remember what we say here, but it can never forget what they did here. It is for us the living, rather, to be dedicated here to the unfinished work which they who fought here have thus far so nobly advanced. It is rather for us to be here dedicated to the great task remaining before us—that from these honored dead we take increased devotion to that cause for which they gave the last full measure of devotion—that we here highly resolve that these dead shall not have died in vain—that this nation, under God, shall have a new birth of freedom—and that government of the people, by the people, for the people, shall not perish from the earth."
	),
	ODE_ON_A_GRECIAN_URN(
		"Thou still unravish'd bride of quietness,",
		"       Thou foster-child of silence and slow time,",
		"Sylvan historian, who canst thus express",
		"       A flowery tale more sweetly than our rhyme:",
		"What leaf-fring'd legend haunts about thy shape",
		"       Of deities or mortals, or of both,",
		"               In Tempe or the dales of Arcady?",
		"       What men or gods are these? What maidens loth?",
		"What mad pursuit? What struggle to escape?",
		"               What pipes and timbrels? What wild ecstasy?",

		"Heard melodies are sweet, but those unheard",
		"       Are sweeter; therefore, ye soft pipes, play on;",
		"Not to the sensual ear, but, more endear'd,",
		"       Pipe to the spirit ditties of no tone:",
		"Fair youth, beneath the trees, thou canst not leave",
		"       Thy song, nor ever can those trees be bare;",
		"               Bold Lover, never, never canst thou kiss,",
		"Though winning near the goal yet, do not grieve;",
		"       She cannot fade, though thou hast not thy bliss,",
		"               For ever wilt thou love, and she be fair!",

		"Ah, happy, happy boughs! that cannot shed",
		"         Your leaves, nor ever bid the Spring adieu;",
		"And, happy melodist, unwearied,",
		"         For ever piping songs for ever new;",
		"More happy love! more happy, happy love!",
		"         For ever warm and still to be enjoy'd,",
		"                For ever panting, and for ever young;",
		"All breathing human passion far above,",
		"         That leaves a heart high-sorrowful and cloy'd,",
		"                A burning forehead, and a parching tongue.",

		"Who are these coming to the sacrifice?",
		"         To what green altar, O mysterious priest,",
		"Lead'st thou that heifer lowing at the skies,",
		"         And all her silken flanks with garlands drest?",
		"What little town by river or sea shore,",
		"         Or mountain-built with peaceful citadel,",
		"                Is emptied of this folk, this pious morn?",
		"And, little town, thy streets for evermore",
		"         Will silent be; and not a soul to tell",
		"                Why thou art desolate, can e'er return.",

		"O Attic shape! Fair attitude! with brede",
		"         Of marble men and maidens overwrought,",
		"With forest branches and the trodden weed;",
		"         Thou, silent form, dost tease us out of thought",
		"As doth eternity: Cold Pastoral!",
		"         When old age shall this generation waste,",
		"                Thou shalt remain, in midst of other woe",
		"Than ours, a friend to man, to whom thou say'st,",
		"         \"Beauty is truth, truth beauty,—that is all",
		"                Ye know on earth, and all ye need to know.\""
	);
	private final TextSection[] textSections;
	private TextSectionResource(String... texts) {
		this.textSections = new TextSection[texts.length];
		for(int i =0; i<texts.length; i++ ) {
			this.textSections[i] = new TextSection(texts[i]);
		}
	}
	public TextSection[] getTextSections() {
		return textSections;
	}
}
