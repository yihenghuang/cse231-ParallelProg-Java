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
package mapreduce.apps.cholera.core;

import edu.wustl.cse231s.IntendedForStaticAccessOnlyError;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public final class SohoCholeraOutbreak1854 {
	private static final CholeraDeath[] deaths;
	static {
		// source:
		// https://www1.udel.edu/johnmack/frec682/cholera/deaths.txt
		// https://www1.udel.edu/johnmack/frec682/cholera/cholera2.html
		Location[] locations = new Location[] { new Location(13.58801, 11.0956), new Location(9.878124, 12.55918),
				new Location(14.65398, 10.18044), new Location(15.22057, 9.993003), new Location(13.16265, 12.96319),
				new Location(13.80617, 8.889046), new Location(13.10214, 10.56081), new Location(11.00403, 11.86713),
				new Location(15.15475, 11.70451), new Location(11.12639, 9.643859), new Location(11.7104, 13.59038),
				new Location(12.34107, 11.48214), new Location(10.58664, 11.86681), new Location(14.56957, 10.57711),
				new Location(16.56765, 14.32268), new Location(9.511439, 10.67225), new Location(13.33699, 10.70585),
				new Location(15.03486, 10.19459), new Location(15.13973, 10.04012), new Location(10.87104, 9.823215),
				new Location(12.54631, 11.9826), new Location(11.82367, 11.7979), new Location(12.19227, 10.38389),
				new Location(13.87589, 12.75045), new Location(12.30798, 11.85122), new Location(10.97379, 11.85359),
				new Location(10.98912, 9.759229), new Location(13.50803, 13.34022), new Location(10.80653, 11.66098),
				new Location(12.20569, 13.64252), new Location(13.89933, 13.98826), new Location(12.51938, 11.61548),
				new Location(15.74902, 12.65131), new Location(12.85563, 9.896143), new Location(12.98866, 10.1677),
				new Location(13.6861, 11.40649), new Location(13.08889, 11.11662), new Location(13.42064, 11.08226),
				new Location(14.75734, 9.368398), new Location(13.205, 13.16882), new Location(9.847669, 12.53263),
				new Location(12.53966, 12.00474), new Location(13.40522, 10.24531), new Location(14.43222, 11.61986),
				new Location(15.98592, 14.18579), new Location(10.8983, 12.01404), new Location(12.54032, 13.41382),
				new Location(15.84175, 12.38042), new Location(16.54906, 14.34902), new Location(11.19962, 8.586758),
				new Location(15.77734, 12.17214), new Location(11.01652, 9.782825), new Location(11.68324, 13.5818),
				new Location(11.54616, 12.27906), new Location(11.79539, 15.07723), new Location(13.04533, 13.88243),
				new Location(14.14244, 13.1333), new Location(14.84649, 10.01118), new Location(12.58271, 11.01057),
				new Location(14.63834, 12.92149), new Location(12.47289, 11.21463), new Location(14.4973, 8.744322),
				new Location(9.224811, 10.83608), new Location(17.93893, 7.189272), new Location(11.17129, 14.73371),
				new Location(9.487955, 10.70568), new Location(10.80275, 9.933474), new Location(16.06981, 14.11234),
				new Location(10.38257, 10.56815), new Location(13.68547, 8.984794), new Location(15.80461, 13.92831),
				new Location(12.17318, 11.80932), new Location(11.49627, 10.73456), new Location(15.40609, 11.19265),
				new Location(15.91103, 12.20705), new Location(10.1881, 11.91827), new Location(14.0004, 12.77549),
				new Location(16.54684, 11.39338), new Location(17.51501, 11.22888), new Location(13.77803, 8.864945),
				new Location(14.11088, 10.56043), new Location(14.68581, 11.45555), new Location(12.57377, 13.43932),
				new Location(11.70552, 10.36466), new Location(14.36912, 12.55025), new Location(15.21281, 16.9593),
				new Location(15.81613, 13.89809), new Location(13.89643, 12.72108), new Location(15.18307, 11.5893),
				new Location(13.17116, 12.932), new Location(12.65285, 11.26382), new Location(15.13931, 13.20189),
				new Location(12.8446, 11.61027), new Location(13.55442, 13.17523), new Location(14.02949, 13.07497),
				new Location(15.43982, 13.27819), new Location(14.84585, 14.86907), new Location(10.27732, 11.36702),
				new Location(10.52977, 11.62937), new Location(9.933102, 12.30593), new Location(14.15951, 9.238947),
				new Location(13.61841, 12.54927), new Location(14.95599, 9.839847), new Location(15.69362, 12.38079),
				new Location(9.200059, 11.97217), new Location(16.83013, 11.39388), new Location(13.31452, 10.31512),
				new Location(10.56304, 11.58176), new Location(8.311067, 7.202524), new Location(13.39306, 13.03761),
				new Location(14.51838, 8.716039), new Location(16.80886, 11.42717), new Location(14.71134, 11.91757),
				new Location(10.31367, 11.38647), new Location(13.15651, 11.15159), new Location(9.083952, 13.21588),
				new Location(15.19022, 9.974836), new Location(8.436085, 7.393596), new Location(15.34814, 9.820333),
				new Location(13.31339, 12.3789), new Location(13.23216, 9.731717), new Location(12.55331, 11.54684),
				new Location(13.89099, 10.95253), new Location(13.49273, 12.51423), new Location(15.24806, 16.97276),
				new Location(11.58812, 11.09262), new Location(9.442908, 10.76749), new Location(11.36395, 9.602994),
				new Location(11.15694, 14.775), new Location(11.89653, 10.2304), new Location(15.01308, 10.21483),
				new Location(13.61045, 11.49483), new Location(14.1069, 13.60306), new Location(10.46296, 11.20981),
				new Location(10.30772, 9.4627), new Location(13.77892, 12.69592), new Location(13.41328, 12.42838),
				new Location(12.56326, 11.04593), new Location(15.66159, 13.93562), new Location(13.83545, 8.909176),
				new Location(13.22668, 12.33923), new Location(15.11869, 10.0674), new Location(13.24604, 10.51404),
				new Location(14.62721, 11.42144), new Location(14.38883, 10.29258), new Location(15.02184, 14.11212),
				new Location(13.14793, 10.59459), new Location(13.39042, 8.81647), new Location(11.77328, 9.525179),
				new Location(16.04067, 14.35417), new Location(10.62561, 10.92979), new Location(14.55994, 10.6003),
				new Location(9.327332, 12.16842), new Location(14.50837, 11.50046), new Location(10.52485, 12.31847),
				new Location(13.9858, 12.80076), new Location(10.55035, 11.602), new Location(10.64812, 11.70659),
				new Location(15.7345, 6.090047), new Location(11.8024, 11.16029), new Location(11.76803, 10.26851),
				new Location(11.57369, 11.1289), new Location(15.76205, 14.02013), new Location(10.6587, 11.67938),
				new Location(13.40605, 13.28477), new Location(11.24757, 11.45161), new Location(15.53659, 12.76087),
				new Location(14.02418, 10.70804), new Location(13.39872, 12.45665), new Location(14.57784, 16.18932),
				new Location(15.32621, 13.43324), new Location(14.47304, 8.777501), new Location(11.39957, 9.887847),
				new Location(12.48731, 11.17735), new Location(13.78123, 11.1606), new Location(10.56813, 11.02786),
				new Location(10.53582, 11.01035), new Location(14.75957, 10.75233), new Location(14.2318, 12.04069),
				new Location(13.34694, 11.97577), new Location(10.43855, 11.80709), new Location(12.51876, 10.58196),
				new Location(14.99982, 11.99344), new Location(10.64479, 9.882927), new Location(13.12517, 11.13206),
				new Location(10.0868, 11.47022), new Location(11.2153, 11.4361), new Location(12.9097, 11.73742),
				new Location(12.17253, 9.965585), new Location(12.44094, 11.28124), new Location(11.9171, 10.20203),
				new Location(11.47169, 11.38392), new Location(11.65302, 13.57026), new Location(12.45562, 11.50935),
				new Location(14.77868, 12.31298), new Location(14.018, 12.74917), new Location(12.50406, 13.39938),
				new Location(9.217884, 12.33342), new Location(10.39745, 11.306), new Location(12.74603, 9.155196),
				new Location(9.105901, 6.343998), new Location(8.342558, 7.136541), new Location(15.31736, 13.56758),
				new Location(11.18297, 11.41759), new Location(11.78689, 11.19158), new Location(12.7135, 11.30091),
				new Location(11.76649, 11.22997), new Location(12.17855, 13.63494), new Location(12.68321, 11.28437),
				new Location(13.62292, 13.38845), new Location(8.812553, 15.13408), new Location(12.22264, 11.33782),
				new Location(16.18479, 14.29076), new Location(12.8446, 11.61027), new Location(13.60953, 13.42772),
				new Location(12.69358, 10.21487), new Location(15.025, 11.85505), new Location(13.91299, 12.69277),
				new Location(13.60802, 9.07513), new Location(12.47179, 13.01132), new Location(9.870624, 12.40407),
				new Location(17.5951, 7.335869), new Location(11.10299, 11.11242), new Location(16.66206, 14.40429),
				new Location(9.693037, 11.00984), new Location(13.40353, 13.00439), new Location(13.12842, 10.58083),
				new Location(13.7408, 10.07836), new Location(11.67779, 11.12924), new Location(13.14523, 11.81876),
				new Location(13.1237, 11.84915), new Location(12.29844, 11.88042), new Location(14.90404, 12.763),
				new Location(11.53711, 12.58766), new Location(12.87284, 11.62384), new Location(11.04585, 11.23151),
				new Location(15.77554, 13.98787), new Location(12.90019, 10.33882), new Location(12.31552, 11.51159),
				new Location(9.794394, 11.77236), new Location(12.65285, 11.26382), new Location(12.67759, 12.0546),
				new Location(15.9603, 9.162499), new Location(11.03126, 11.87971), new Location(14.08877, 9.337553),
				new Location(11.0271, 11.74557), new Location(14.54157, 8.684826), new Location(15.55098, 11.28022),
				new Location(9.579331, 11.78081), new Location(15.46573, 11.09663), new Location(14.44191, 12.15951),
				new Location(11.38291, 9.910148), new Location(13.74064, 11.31248), new Location(11.39099, 11.03369),
				new Location(16.01572, 9.072348), new Location(9.921976, 11.86345), new Location(10.80179, 9.687121),
				new Location(12.6326, 11.62268), new Location(9.262066, 10.66323), new Location(13.78464, 13.81561),
				new Location(13.75736, 13.79555), new Location(14.70788, 9.969954), new Location(15.29153, 10.04976),
				new Location(14.14686, 12.42262), new Location(13.76823, 10.09442), new Location(10.9726, 11.34485),
				new Location(12.36505, 10.48427), new Location(14.81631, 10.04214), new Location(15.04986, 13.61393),
				new Location(15.36158, 13.45471), new Location(12.793, 14.31211), new Location(11.324, 9.79492),
				new Location(10.45107, 11.77685), new Location(11.01349, 11.211), new Location(13.38261, 9.455654),
				new Location(10.33884, 11.39608), new Location(9.610787, 11.80735), new Location(14.54691, 12.2733),
				new Location(11.45123, 11.4183), new Location(12.74049, 10.73864), new Location(14.95969, 11.96889),
				new Location(12.31692, 14.83151), new Location(12.88335, 10.78442), new Location(15.2704, 9.139883),
				new Location(15.77505, 9.566566), new Location(13.06382, 13.85009), new Location(12.92922, 11.70606),
				new Location(15.28385, 12.10795), new Location(13.01865, 11.78975), new Location(12.45943, 11.2489),
				new Location(15.76892, 12.95444), new Location(14.03004, 13.35564), new Location(12.98116, 14.37319),
				new Location(13.8822, 14.63515), new Location(10.93752, 9.725004), new Location(16.02412, 14.00992),
				new Location(15.09569, 10.09361), new Location(13.46457, 12.56776), new Location(11.58543, 12.29345),
				new Location(10.69892, 12.00221), new Location(16.8394, 11.6017), new Location(13.90569, 14.60172),
				new Location(13.70339, 11.11278), new Location(13.28895, 9.631591), new Location(15.6604, 12.3693),
				new Location(15.46487, 11.2269), new Location(10.99125, 11.19733), new Location(15.50104, 11.11599),
				new Location(11.70601, 9.637946), new Location(12.35546, 11.44285), new Location(11.12872, 11.52963),
				new Location(9.595812, 10.9393), new Location(8.325407, 7.166975), new Location(14.00061, 13.33951),
				new Location(13.24529, 12.99792), new Location(15.28504, 9.337882), new Location(15.4973, 11.24314),
				new Location(15.38694, 7.330029), new Location(16.05814, 14.31984), new Location(12.3847, 13.69473),
				new Location(12.18887, 11.78904), new Location(15.12332, 11.68831), new Location(13.95376, 12.86237),
				new Location(9.618408, 10.91289), new Location(16.29265, 14.33811), new Location(12.64394, 10.64805),
				new Location(10.49377, 12.87479), new Location(13.97831, 12.83293), new Location(15.52151, 11.26509),
				new Location(10.96399, 11.18275), new Location(13.29801, 12.91595), new Location(13.91508, 13.56587),
				new Location(13.31653, 9.643641), new Location(12.74707, 10.7125), new Location(15.44155, 11.208),
				new Location(15.69372, 13.87902), new Location(13.83759, 11.05554), new Location(11.67728, 10.47627),
				new Location(16.07268, 14.29056), new Location(12.59532, 10.98533), new Location(15.35726, 11.29407),
				new Location(16.284, 14.36028), new Location(12.50162, 12.06644), new Location(15.65, 12.90427),
				new Location(11.54975, 12.56442), new Location(14.21522, 12.068), new Location(16.25294, 10.09349),
				new Location(13.62379, 11.20419), new Location(12.61527, 10.3553), new Location(11.65745, 11.17163),
				new Location(8.771104, 10.94251), new Location(10.9025, 14.69996), new Location(11.61652, 11.05411),
				new Location(13.03883, 11.24061), new Location(13.70734, 11.29599), new Location(11.06231, 11.75703),
				new Location(14.57227, 12.29128), new Location(10.43663, 11.56376), new Location(13.1377, 12.28358),
				new Location(11.87974, 14.16151), new Location(13.33869, 10.33806), new Location(9.854939, 12.17599),
				new Location(12.74439, 12.84671), new Location(9.285564, 10.63081), new Location(11.59055, 9.53944),
				new Location(13.1202, 12.3159), new Location(11.40942, 10.99734), new Location(13.63869, 11.5084),
				new Location(11.76579, 13.61152), new Location(16.26833, 14.38156), new Location(8.73591, 12.05172),
				new Location(15.74913, 12.16057), new Location(12.18563, 11.40151), new Location(14.78633, 14.09387),
				new Location(13.45084, 8.860812), new Location(15.73667, 12.94093), new Location(11.91194, 14.17201),
				new Location(12.5272, 12.7851), new Location(14.3738, 13.07674), new Location(12.21672, 14.14109),
				new Location(15.28043, 11.42152), new Location(11.53864, 10.75491), new Location(9.046618, 11.20749),
				new Location(10.75256, 8.843783), new Location(14.9461, 10.10509), new Location(13.22677, 11.90959),
				new Location(13.41574, 8.835455), new Location(14.35785, 12.03667), new Location(12.20415, 11.37017),
				new Location(12.00341, 14.56914), new Location(13.58721, 11.60537), new Location(8.757473, 12.02333),
				new Location(13.38881, 10.9706), new Location(12.57046, 12.79843), new Location(15.56334, 14.17455),
				new Location(12.52108, 12.03208), new Location(12.40901, 11.34986), new Location(12.36903, 11.4156),
				new Location(13.44909, 12.60106), new Location(12.03297, 11.36891), new Location(9.944605, 11.83905),
				new Location(12.14346, 13.6315), new Location(14.40612, 13.09598), new Location(15.70642, 12.9274),
				new Location(13.75891, 11.14193), new Location(11.44538, 10.93066), new Location(13.7965, 14.16886),
				new Location(15.57711, 11.30424), new Location(14.99393, 10.24519), new Location(14.1129, 13.12017),
				new Location(13.17422, 9.835811), new Location(13.26078, 12.96562), new Location(10.93261, 12.03151),
				new Location(12.14729, 9.951969), new Location(13.95731, 13.88316), new Location(11.26415, 10.80238),
				new Location(13.42597, 12.96798), new Location(13.51533, 12.48783), new Location(14.7689, 11.9578),
				new Location(12.9731, 10.95023), new Location(12.42551, 11.31754), new Location(12.34805, 14.77593),
				new Location(13.10475, 12.3512), new Location(14.77894, 13.19966), new Location(9.813148, 12.50214),
				new Location(14.14413, 10.57392), new Location(12.03469, 14.58466), new Location(16.09884, 14.23706),
				new Location(12.68321, 11.28437), new Location(14.69502, 10.12684), new Location(8.840331, 12.13417),
				new Location(10.54701, 12.32713), new Location(12.24902, 14.15759), new Location(11.73556, 13.59899),
				new Location(8.280715, 11.56829), new Location(12.38503, 13.21802), new Location(16.73377, 8.447514),
				new Location(15.72296, 7.17813), new Location(16.08321, 14.26035), new Location(14.27252, 10.4667),
				new Location(9.736776, 12.35915), new Location(13.28041, 12.94127), new Location(16.06733, 9.970328),
				new Location(14.35376, 10.35223), new Location(13.40536, 10.94129), new Location(14.51969, 9.029811),
				new Location(14.57164, 11.38928), new Location(13.71177, 13.9442), new Location(16.23636, 14.1988),
				new Location(16.25586, 14.41581), new Location(12.80755, 14.28284), new Location(13.3605, 11.94751),
				new Location(11.17751, 8.616005), new Location(11.64155, 12.17337), new Location(14.73854, 11.93963),
				new Location(9.423313, 10.79384), new Location(9.466473, 10.73907), new Location(15.71121, 11.60836),
				new Location(14.26207, 12.49088), new Location(14.18834, 13.43268), new Location(11.48419, 10.41928),
				new Location(15.02863, 10.36174), new Location(13.08718, 12.37853), new Location(10.95782, 12.04313),
				new Location(11.939, 14.17458), new Location(13.48189, 8.887023), new Location(14.06296, 13.09025),
				new Location(10.94726, 16.4859), new Location(14.20514, 9.438095), new Location(10.38407, 11.159),
				new Location(15.60473, 11.31529), new Location(11.27275, 11.46122), new Location(13.02248, 9.609664),
				new Location(13.57891, 13.14179), new Location(11.07647, 11.64163), new Location(14.5969, 11.4039),
				new Location(15.24637, 8.629816), new Location(13.36475, 10.36409), new Location(16.14358, 10.90531),
				new Location(14.299, 10.43223), new Location(16.41171, 14.39729), new Location(12.59053, 14.97835),
				new Location(13.25468, 10.81906), new Location(10.99315, 12.06159), new Location(10.10162, 10.64977),
				new Location(15.62137, 12.55621), new Location(14.21592, 13.44473), new Location(13.36865, 12.51622),
				new Location(10.77831, 11.64841), new Location(13.17832, 10.61175), new Location(13.80602, 12.6384),
				new Location(11.42396, 10.96806), new Location(12.91849, 9.794238), new Location(13.60765, 11.07225),
				new Location(13.33782, 14.33142), new Location(12.16919, 11.43683), new Location(14.36735, 10.32597),
				new Location(8.863929, 12.10775), new Location(15.54189, 14.20994), new Location(14.3156, 10.40692),
				new Location(10.02774, 11.28289), new Location(13.20859, 14.26238), new Location(10.48503, 12.45631),
				new Location(9.53302, 10.64486), new Location(10.43369, 11.19426), new Location(10.92631, 14.62447),
				new Location(13.3186, 11.02381), new Location(12.6148, 10.95197), new Location(13.08049, 14.20134),
				new Location(11.64594, 10.45675), new Location(13.56385, 10.07179), new Location(13.37551, 13.06693),
				new Location(13.7367, 11.13027), new Location(10.68826, 12.45906), new Location(12.18764, 14.13755),
				new Location(13.96921, 13.32231), new Location(14.77023, 9.210511), new Location(15.08261, 10.12219),
				new Location(16.23659, 9.914474), new Location(15.31356, 11.4458), new Location(12.54972, 11.07519),
				new Location(14.65652, 11.439), new Location(12.53218, 11.10452), new Location(10.91304, 14.67075),
				new Location(15.67918, 12.91381), new Location(11.65278, 12.62286), new Location(9.501461, 11.54471),
				new Location(13.26943, 14.79979), new Location(13.61611, 15.32844), new Location(13.43057, 10.2633),
				new Location(13.48013, 12.53947), new Location(11.07184, 11.10491), new Location(12.92878, 10.93291),
				new Location(12.84456, 16.01885), new Location(15.31608, 9.794086), new Location(12.78222, 11.34187),
				new Location(9.207745, 11.08172), new Location(14.37433, 12.00336), new Location(15.58141, 10.92052),
				new Location(10.40739, 11.17765), new Location(12.98045, 10.66068), new Location(13.24918, 11.87218),
				new Location(10.96288, 9.745629), new Location(13.51754, 11.0086), new Location(9.582069, 10.58299),
				new Location(14.53736, 10.6277), new Location(12.51176, 11.1419), new Location(10.71447, 12.47066),
				new Location(13.3154, 12.87661), new Location(9.378991, 10.9628), new Location(13.37906, 11.91917),
				new Location(13.52166, 11.13973), new Location(13.49601, 11.03899), new Location(13.63715, 9.099265),
				new Location(14.26445, 15.6235), new Location(9.556584, 10.61644), new Location(14.07245, 15.51041),
				new Location(12.94471, 13.7879), new Location(15.41332, 13.8084), new Location(13.78938, 12.6617),
				new Location(11.41806, 9.855504), new Location(12.39045, 11.3782), new Location(12.74481, 11.31843),
				new Location(13.58522, 11.48222), new Location(14.08854, 13.10222), new Location(15.06661, 10.14866),
				new Location(15.27804, 13.55018), new Location(14.67613, 10.15019), new Location(13.37922, 12.48901),
				new Location(12.27691, 11.91081), new Location(14.33308, 10.37359), new Location(12.42151, 11.56698),
				new Location(12.06085, 10.30288), new Location(12.43804, 11.53667), new Location(15.05064, 10.17412),
				new Location(17.27166, 11.6338), new Location(12.4261, 11.91442), new Location(15.01817, 12.51581) };

		deaths = new CholeraDeath[locations.length];
		for(int i=0; i<locations.length; i++) {
			deaths[i] = new CholeraDeath(locations[i]);
		}
	}

	private SohoCholeraOutbreak1854() {
		throw new IntendedForStaticAccessOnlyError();
	}

	public static CholeraDeath[] getDeaths() {
		return deaths;
	}
}
