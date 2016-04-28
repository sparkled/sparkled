-- DDL.
DROP SCHEMA IF EXISTS sparkled CASCADE;
CREATE SCHEMA sparkled;

CREATE TABLE sparkled.song (
  id SERIAL PRIMARY KEY,
  album VARCHAR(64) NOT NULL,
  animation_data TEXT,
  artist VARCHAR(64) NOT NULL,
  duration_seconds INTEGER NOT NULL,
  name VARCHAR(64) NOT NULL
);

CREATE TABLE sparkled.song_data (
  song_id INTEGER PRIMARY KEY NOT NULL,
  mp3_data BYTEA NOT NULL
);

CREATE TABLE sparkled.stage (
  id SERIAL PRIMARY KEY,
  svg TEXT NOT NULL
);

-- Data insert.
INSERT INTO sparkled.stage (id, svg) VALUES
  (1, '<svg xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" version="1.1" x="0px" y="0px" width="800px" height="450px" viewBox="0 0 800 450" enable-background="new 0 0 800 450" xml:space="preserve"><style type="text/css">circle {fill: none;stroke: #ebebeb;stroke-miterlimit: 10;r: 2}.pillar rect {fill: rgba(255,255,255,.05);stroke: #4e5d6c;stroke-width: 2;stroke-miterlimit: 10}</style><g><g><g><path fill="none" stroke="#4e5d6c" stroke-width="2" stroke-miterlimit="10" d="M205.768,423c0-48.601-40.389-88-89.033-88s-88.078,39.399-88.078,88"></path><g><circle id="led-0" cx="28.657" cy="423"></circle><circle id="led-1" cx="29.018" cy="414.972"></circle><circle id="led-2" cx="30.082" cy="407.146"></circle><circle id="led-3" cx="31.817" cy="399.555"></circle><circle id="led-4" cx="34.192" cy="392.228"></circle><circle id="led-5" cx="37.175" cy="385.197"></circle><circle id="led-6" cx="40.735" cy="378.495"></circle><circle id="led-7" cx="44.841" cy="372.151"></circle><circle id="led-8" cx="49.461" cy="366.197"></circle><circle id="led-9" cx="54.564" cy="360.664"></circle><circle id="led-10" cx="60.12" cy="355.585"></circle><circle id="led-11" cx="66.096" cy="350.989"></circle><circle id="led-12" cx="72.461" cy="346.909"></circle><circle id="led-13" cx="79.184" cy="343.375"></circle><circle id="led-14" cx="86.233" cy="340.42"></circle><circle id="led-15" cx="93.578" cy="338.073"></circle><circle id="led-16" cx="101.187" cy="336.367"></circle><circle id="led-17" cx="109.028" cy="335.332"></circle><circle id="led-18" cx="117.069" cy="335.001"></circle><circle id="led-19" cx="125.07" cy="335.388"></circle><circle id="led-20" cx="132.883" cy="336.47"></circle><circle id="led-21" cx="140.478" cy="338.214"></circle><circle id="led-22" cx="147.821" cy="340.592"></circle><circle id="led-23" cx="154.878" cy="343.57"></circle><circle id="led-24" cx="161.618" cy="347.12"></circle><circle id="led-25" cx="168.008" cy="351.209"></circle><circle id="led-26" cx="174.014" cy="355.808"></circle><circle id="led-27" cx="179.604" cy="360.884"></circle><circle id="led-28" cx="184.745" cy="366.408"></circle><circle id="led-29" cx="189.404" cy="372.349"></circle><circle id="led-30" cx="193.548" cy="378.675"></circle><circle id="led-31" cx="197.145" cy="385.355"></circle><circle id="led-32" cx="200.162" cy="392.36"></circle><circle id="led-33" cx="202.565" cy="399.657"></circle><circle id="led-34" cx="204.323" cy="407.217"></circle><circle id="led-35" cx="205.401" cy="415.008"></circle><circle id="led-36" cx="205.768" cy="423"></circle></g></g></g><g><g><path fill="none" stroke="#4e5d6c" stroke-width="2" stroke-miterlimit="10" d="M392.845,423c0-48.601-40.389-88-89.033-88s-88.078,39.399-88.078,88"></path><g><circle id="led-37" cx="215.734" cy="423"></circle><circle id="led-38" cx="216.096" cy="414.972"></circle><circle id="led-39" cx="217.159" cy="407.146"></circle><circle id="led-40" cx="218.894" cy="399.555"></circle><circle id="led-41" cx="221.269" cy="392.228"></circle><circle id="led-42" cx="224.252" cy="385.197"></circle><circle id="led-43" cx="227.812" cy="378.495"></circle><circle id="led-44" cx="231.918" cy="372.151"></circle><circle id="led-45" cx="236.538" cy="366.197"></circle><circle id="led-46" cx="241.642" cy="360.664"></circle><circle id="led-47" cx="247.197" cy="355.585"></circle><circle id="led-48" cx="253.173" cy="350.989"></circle><circle id="led-49" cx="259.538" cy="346.909"></circle><circle id="led-50" cx="266.261" cy="343.375"></circle><circle id="led-51" cx="273.311" cy="340.42"></circle><circle id="led-52" cx="280.655" cy="338.073"></circle><circle id="led-53" cx="288.264" cy="336.367"></circle><circle id="led-54" cx="296.106" cy="335.332"></circle><circle id="led-55" cx="304.147" cy="335.001"></circle><circle id="led-56" cx="312.147" cy="335.388"></circle><circle id="led-57" cx="319.961" cy="336.47"></circle><circle id="led-58" cx="327.555" cy="338.214"></circle><circle id="led-59" cx="334.898" cy="340.592"></circle><circle id="led-60" cx="341.956" cy="343.57"></circle><circle id="led-61" cx="348.696" cy="347.12"></circle><circle id="led-62" cx="355.085" cy="351.209"></circle><circle id="led-63" cx="361.091" cy="355.808"></circle><circle id="led-64" cx="366.681" cy="360.884"></circle><circle id="led-65" cx="371.822" cy="366.408"></circle><circle id="led-66" cx="376.481" cy="372.349"></circle><circle id="led-67" cx="380.626" cy="378.675"></circle><circle id="led-68" cx="384.223" cy="385.355"></circle><circle id="led-69" cx="387.239" cy="392.36"></circle><circle id="led-70" cx="389.643" cy="399.657"></circle><circle id="led-71" cx="391.4" cy="407.217"></circle><circle id="led-72" cx="392.478" cy="415.008"></circle><circle id="led-73" cx="392.845" cy="423"></circle></g></g></g><g><g><path fill="none" stroke="#4e5d6c" stroke-width="2" stroke-miterlimit="10" d="M579.923,423c0-48.601-40.389-88-89.033-88s-88.078,39.399-88.078,88"></path><g><circle id="led-74" cx="402.811" cy="423"></circle><circle id="led-75" cx="403.173" cy="414.972"></circle><circle id="led-76" cx="404.237" cy="407.146"></circle><circle id="led-77" cx="405.972" cy="399.555"></circle><circle id="led-78" cx="408.346" cy="392.228"></circle><circle id="led-79" cx="411.329" cy="385.197"></circle><circle id="led-80" cx="414.89" cy="378.495"></circle><circle id="led-81" cx="418.995" cy="372.151"></circle><circle id="led-82" cx="423.616" cy="366.197"></circle><circle id="led-83" cx="428.719" cy="360.664"></circle><circle id="led-84" cx="434.275" cy="355.585"></circle><circle id="led-85" cx="440.25" cy="350.989"></circle><circle id="led-86" cx="446.615" cy="346.909"></circle><circle id="led-87" cx="453.338" cy="343.375"></circle><circle id="led-88" cx="460.388" cy="340.42"></circle><circle id="led-89" cx="467.733" cy="338.073"></circle><circle id="led-90" cx="475.342" cy="336.367"></circle><circle id="led-91" cx="483.183" cy="335.332"></circle><circle id="led-92" cx="491.224" cy="335.001"></circle><circle id="led-93" cx="499.224" cy="335.388"></circle><circle id="led-94" cx="507.038" cy="336.47"></circle><circle id="led-95" cx="514.633" cy="338.214"></circle><circle id="led-96" cx="521.975" cy="340.592"></circle><circle id="led-97" cx="529.033" cy="343.57"></circle><circle id="led-98" cx="535.773" cy="347.12"></circle><circle id="led-99" cx="542.163" cy="351.209"></circle><circle id="led-100" cx="548.169" cy="355.808"></circle><circle id="led-101" cx="553.759" cy="360.884"></circle><circle id="led-102" cx="558.9" cy="366.408"></circle><circle id="led-103" cx="563.559" cy="372.349"></circle><circle id="led-104" cx="567.703" cy="378.675"></circle><circle id="led-105" cx="571.3" cy="385.355"></circle><circle id="led-106" cx="574.317" cy="392.36"></circle><circle id="led-107" cx="576.72" cy="399.657"></circle><circle id="led-108" cx="578.477" cy="407.217"></circle><circle id="led-109" cx="579.556" cy="415.008"></circle><circle id="led-110" cx="579.923" cy="423"></circle></g></g></g><g><g><path fill="none" stroke="#4e5d6c" stroke-width="2" stroke-miterlimit="10" d="M767,423c0-48.601-40.389-88-89.033-88c-48.644,0-88.078,39.399-88.078,88"></path><g><circle id="led-111" cx="589.889" cy="423"></circle><circle id="led-112" cx="590.25" cy="414.972"></circle><circle id="led-113" cx="591.314" cy="407.146"></circle><circle id="led-114" cx="593.049" cy="399.555"></circle><circle id="led-115" cx="595.424" cy="392.228"></circle><circle id="led-116" cx="598.407" cy="385.197"></circle><circle id="led-117" cx="601.967" cy="378.495"></circle><circle id="led-118" cx="606.073" cy="372.151"></circle><circle id="led-119" cx="610.693" cy="366.197"></circle><circle id="led-120" cx="615.797" cy="360.664"></circle><circle id="led-121" cx="621.352" cy="355.585"></circle><circle id="led-122" cx="627.328" cy="350.989"></circle><circle id="led-123" cx="633.693" cy="346.909"></circle><circle id="led-124" cx="640.416" cy="343.375"></circle><circle id="led-125" cx="647.465" cy="340.42"></circle><circle id="led-126" cx="654.81" cy="338.073"></circle><circle id="led-127" cx="662.419" cy="336.367"></circle><circle id="led-128" cx="670.261" cy="335.332"></circle><circle id="led-129" cx="678.302" cy="335.001"></circle><circle id="led-130" cx="686.302" cy="335.388"></circle><circle id="led-131" cx="694.116" cy="336.47"></circle><circle id="led-132" cx="701.71" cy="338.214"></circle><circle id="led-133" cx="709.053" cy="340.592"></circle><circle id="led-134" cx="716.111" cy="343.57"></circle><circle id="led-135" cx="722.851" cy="347.12"></circle><circle id="led-136" cx="729.24" cy="351.209"></circle><circle id="led-137" cx="735.246" cy="355.808"></circle><circle id="led-138" cx="740.836" cy="360.884"></circle><circle id="led-139" cx="745.977" cy="366.408"></circle><circle id="led-140" cx="750.636" cy="372.349"></circle><circle id="led-141" cx="754.781" cy="378.675"></circle><circle id="led-142" cx="758.378" cy="385.355"></circle><circle id="led-143" cx="761.394" cy="392.36"></circle><circle id="led-144" cx="763.797" cy="399.657"></circle><circle id="led-145" cx="765.555" cy="407.217"></circle><circle id="led-146" cx="766.633" cy="415.008"></circle><circle id="led-147" cx="767" cy="423"></circle></g></g></g></g><g><g><g class="pillar"><rect x="34.05" y="34.6" width="42" height="230.5"></rect><rect x="26.657" y="265.42" width="56.786" height="6.773"></rect><rect x="26.657" y="27.807" width="56.786" height="6.773"></rect></g><g><path fill="none" stroke="#4e5d6c" stroke-width="2" stroke-miterlimit="10" d="M33.941,265.001c0-10.426,46.109-10.669,46.109-21.095c0-10.427-50-10.427-50-20.854c0-10.425,50-10.425,50-20.851c0-10.426-50-10.426-50-20.853c0-10.427,50-10.427,50-20.853c0-10.428-50-10.428-50-20.856c0-10.428,50-10.428,50-20.856c0-10.428-50-10.428-50-20.856c0-10.428,50-10.428,50-20.857c0-10.43-50-10.43-50-20.86s46-10.43,46-20.86"></path><g><circle id="led-148" cx="33.941" cy="265.001"></circle><circle id="led-149" cx="40.459" cy="259.042"></circle><circle id="led-150" cx="55.133" cy="254.885"></circle><circle id="led-151" cx="70.642" cy="250.912"></circle><circle id="led-152" cx="79.662" cy="245.506"></circle><circle id="led-153" cx="76.149" cy="239.36"></circle><circle id="led-154" cx="62.935" cy="235.174"></circle><circle id="led-155" cx="46.566" cy="231.646"></circle><circle id="led-156" cx="33.6" cy="227.409"></circle><circle id="led-157" cx="30.551" cy="221.312"></circle><circle id="led-158" cx="39.667" cy="216.273"></circle><circle id="led-159" cx="55.258" cy="212.583"></circle><circle id="led-160" cx="70.767" cy="208.876"></circle><circle id="led-161" cx="79.637" cy="203.785"></circle><circle id="led-162" cx="76.269" cy="197.719"></circle><circle id="led-163" cx="63.138" cy="193.516"></circle><circle id="led-164" cx="46.768" cy="189.989"></circle><circle id="led-165" cx="33.717" cy="185.769"></circle><circle id="led-166" cx="30.504" cy="179.688"></circle><circle id="led-167" cx="39.495" cy="174.621"></circle><circle id="led-168" cx="55.044" cy="170.923"></circle><circle id="led-169" cx="70.595" cy="167.224"></circle><circle id="led-170" cx="79.593" cy="162.16"></circle><circle id="led-171" cx="76.389" cy="156.077"></circle><circle id="led-172" cx="63.343" cy="151.856"></circle><circle id="led-173" cx="46.973" cy="148.328"></circle><circle id="led-174" cx="33.837" cy="144.126"></circle><circle id="led-175" cx="30.46" cy="138.059"></circle><circle id="led-176" cx="39.321" cy="132.965"></circle><circle id="led-177" cx="54.827" cy="129.257"></circle><circle id="led-178" cx="70.42" cy="125.567"></circle><circle id="led-179" cx="79.545" cy="120.529"></circle><circle id="led-180" cx="76.509" cy="114.431"></circle><circle id="led-181" cx="63.55" cy="110.191"></circle><circle id="led-182" cx="47.182" cy="106.663"></circle><circle id="led-183" cx="33.961" cy="102.479"></circle><circle id="led-184" cx="30.417" cy="96.429"></circle><circle id="led-185" cx="39.148" cy="91.308"></circle><circle id="led-186" cx="54.608" cy="87.59"></circle><circle id="led-187" cx="70.243" cy="83.909"></circle><circle id="led-188" cx="79.495" cy="78.897"></circle><circle id="led-189" cx="76.627" cy="72.784"></circle><circle id="led-190" cx="63.757" cy="68.525"></circle><circle id="led-191" cx="47.392" cy="64.996"></circle><circle id="led-192" cx="34.087" cy="60.829"></circle><circle id="led-193" cx="30.392" cy="54.702"></circle><circle id="led-194" cx="39.298" cy="49.296"></circle><circle id="led-195" cx="54.796" cy="45.383"></circle><circle id="led-196" cx="69.506" cy="41.293"></circle><circle id="led-197" cx="76.05" cy="35.35"></circle></g></g></g><g><g class="pillar"><rect x="262.902" y="34.6" width="42" height="230.5"></rect><rect x="255.509" y="265.42" width="56.786" height="6.773"></rect><rect x="255.509" y="27.807" width="56.786" height="6.773"></rect></g><g><path fill="none" stroke="#4e5d6c" stroke-width="2" stroke-miterlimit="10" d="M262.793,265.001c0-10.426,46.109-10.669,46.109-21.095c0-10.427-50-10.427-50-20.854c0-10.425,50-10.425,50-20.851c0-10.426-50-10.426-50-20.853c0-10.427,50-10.427,50-20.853c0-10.428-50-10.428-50-20.856c0-10.428,50-10.428,50-20.856c0-10.428-50-10.428-50-20.856c0-10.428,50-10.428,50-20.857c0-10.43-50-10.43-50-20.86s46-10.43,46-20.86"></path><g><circle id="led-198" cx="262.793" cy="265.001"></circle><circle id="led-199" cx="269.312" cy="259.042"></circle><circle id="led-200" cx="283.986" cy="254.885"></circle><circle id="led-201" cx="299.494" cy="250.912"></circle><circle id="led-202" cx="308.515" cy="245.506"></circle><circle id="led-203" cx="305.001" cy="239.36"></circle><circle id="led-204" cx="291.787" cy="235.174"></circle><circle id="led-205" cx="275.419" cy="231.646"></circle><circle id="led-206" cx="262.452" cy="227.409"></circle><circle id="led-207" cx="259.403" cy="221.312"></circle><circle id="led-208" cx="268.519" cy="216.273"></circle><circle id="led-209" cx="284.11" cy="212.583"></circle><circle id="led-210" cx="299.619" cy="208.876"></circle><circle id="led-211" cx="308.489" cy="203.785"></circle><circle id="led-212" cx="305.122" cy="197.719"></circle><circle id="led-213" cx="291.99" cy="193.516"></circle><circle id="led-214" cx="275.621" cy="189.989"></circle><circle id="led-215" cx="262.569" cy="185.769"></circle><circle id="led-216" cx="259.357" cy="179.688"></circle><circle id="led-217" cx="268.347" cy="174.621"></circle><circle id="led-218" cx="283.896" cy="170.923"></circle><circle id="led-219" cx="299.448" cy="167.224"></circle><circle id="led-220" cx="308.445" cy="162.16"></circle><circle id="led-221" cx="305.241" cy="156.077"></circle><circle id="led-222" cx="292.195" cy="151.856"></circle><circle id="led-223" cx="275.826" cy="148.328"></circle><circle id="led-224" cx="262.69" cy="144.126"></circle><circle id="led-225" cx="259.312" cy="138.059"></circle><circle id="led-226" cx="268.174" cy="132.965"></circle><circle id="led-227" cx="283.679" cy="129.257"></circle><circle id="led-228" cx="299.272" cy="125.567"></circle><circle id="led-229" cx="308.398" cy="120.529"></circle><circle id="led-230" cx="305.361" cy="114.431"></circle><circle id="led-231" cx="292.402" cy="110.191"></circle><circle id="led-232" cx="276.034" cy="106.663"></circle><circle id="led-233" cx="262.813" cy="102.479"></circle><circle id="led-234" cx="259.269" cy="96.429"></circle><circle id="led-235" cx="268" cy="91.308"></circle><circle id="led-236" cx="283.461" cy="87.59"></circle><circle id="led-237" cx="299.095" cy="83.909"></circle><circle id="led-230.5" cx="308.348" cy="78.897"></circle><circle id="led-239" cx="305.48" cy="72.784"></circle><circle id="led-240" cx="292.61" cy="68.525"></circle><circle id="led-241" cx="276.244" cy="64.996"></circle><circle id="led-242" cx="262.939" cy="60.829"></circle><circle id="led-243" cx="259.244" cy="54.702"></circle><circle id="led-244" cx="268.151" cy="49.296"></circle><circle id="led-245" cx="283.648" cy="45.383"></circle><circle id="led-246" cx="298.359" cy="41.293"></circle><circle id="led-247" cx="304.902" cy="35.35"></circle></g></g></g><g><g class="pillar"><rect x="490.754" y="34.6" width="42" height="230.5"></rect><rect x="483.361" y="265.42" width="56.786" height="6.773"></rect><rect x="483.361" y="27.807" width="56.786" height="6.773"></rect></g><g><path fill="none" stroke="#4e5d6c" stroke-width="2" stroke-miterlimit="10" d="M490.645,265.001c0-10.426,46.109-10.669,46.109-21.095c0-10.427-50-10.427-50-20.854c0-10.425,50-10.425,50-20.851c0-10.426-50-10.426-50-20.853c0-10.427,50-10.427,50-20.853c0-10.428-50-10.428-50-20.856c0-10.428,50-10.428,50-20.856c0-10.428-50-10.428-50-20.856c0-10.428,50-10.428,50-20.857c0-10.43-50-10.43-50-20.86s46-10.43,46-20.86"></path><g><circle id="led-248" cx="490.645" cy="265.001"></circle><circle id="led-249" cx="497.164" cy="259.042"></circle><circle id="led-250" cx="511.838" cy="254.885"></circle><circle id="led-251" cx="527.346" cy="250.912"></circle><circle id="led-252" cx="536.367" cy="245.506"></circle><circle id="led-253" cx="532.854" cy="239.36"></circle><circle id="led-254" cx="519.64" cy="235.174"></circle><circle id="led-255" cx="503.271" cy="231.646"></circle><circle id="led-256" cx="490.305" cy="227.409"></circle><circle id="led-257" cx="487.255" cy="221.312"></circle><circle id="led-258" cx="496.371" cy="216.273"></circle><circle id="led-259" cx="511.962" cy="212.583"></circle><circle id="led-260" cx="527.471" cy="208.876"></circle><circle id="led-261" cx="536.342" cy="203.785"></circle><circle id="led-262" cx="532.974" cy="197.719"></circle><circle id="led-263" cx="519.843" cy="193.516"></circle><circle id="led-264" cx="503.473" cy="189.989"></circle><circle id="led-265" cx="490.422" cy="185.769"></circle><circle id="led-266" cx="487.209" cy="179.688"></circle><circle id="led-267" cx="496.2" cy="174.621"></circle><circle id="led-268" cx="511.749" cy="170.923"></circle><circle id="led-269" cx="527.3" cy="167.224"></circle><circle id="led-270" cx="536.297" cy="162.16"></circle><circle id="led-271" cx="533.094" cy="156.077"></circle><circle id="led-272" cx="520.047" cy="151.856"></circle><circle id="led-273" cx="503.678" cy="148.328"></circle><circle id="led-274" cx="490.542" cy="144.126"></circle><circle id="led-275" cx="487.164" cy="138.059"></circle><circle id="led-276" cx="496.026" cy="132.965"></circle><circle id="led-277" cx="511.531" cy="129.257"></circle><circle id="led-278" cx="527.125" cy="125.567"></circle><circle id="led-279" cx="536.25" cy="120.529"></circle><circle id="led-280" cx="533.214" cy="114.431"></circle><circle id="led-281" cx="520.254" cy="110.191"></circle><circle id="led-282" cx="503.886" cy="106.663"></circle><circle id="led-283" cx="490.665" cy="102.479"></circle><circle id="led-284" cx="487.122" cy="96.429"></circle><circle id="led-285" cx="495.853" cy="91.308"></circle><circle id="led-286" cx="511.313" cy="87.59"></circle><circle id="led-287" cx="526.947" cy="83.909"></circle><circle id="led-288" cx="536.2" cy="78.897"></circle><circle id="led-289" cx="533.332" cy="72.784"></circle><circle id="led-290" cx="520.462" cy="68.525"></circle><circle id="led-291" cx="504.097" cy="64.996"></circle><circle id="led-292" cx="490.792" cy="60.829"></circle><circle id="led-293" cx="487.097" cy="54.702"></circle><circle id="led-294" cx="496.003" cy="49.296"></circle><circle id="led-295" cx="511.501" cy="45.383"></circle><circle id="led-296" cx="526.211" cy="41.293"></circle><circle id="led-297" cx="532.754" cy="35.35"></circle></g></g></g><g><g class="pillar"><rect x="719.607" y="34.6" width="42" height="230.5"></rect><rect x="712.214" y="265.42" width="56.786" height="6.773"></rect><rect x="712.214" y="27.807" width="56.786" height="6.773"></rect></g><g><path fill="none" stroke="#4e5d6c" stroke-width="2" stroke-miterlimit="10" d="M719.498,265.001c0-10.426,46.109-10.669,46.109-21.095c0-10.427-50-10.427-50-20.854c0-10.425,50-10.425,50-20.851c0-10.426-50-10.426-50-20.853c0-10.427,50-10.427,50-20.853c0-10.428-50-10.428-50-20.856c0-10.428,50-10.428,50-20.856c0-10.428-50-10.428-50-20.856c0-10.428,50-10.428,50-20.857c0-10.43-50-10.43-50-20.86s46-10.43,46-20.86"></path><g><circle id="led-298" cx="719.498" cy="265.001"></circle><circle id="led-299" cx="726.016" cy="259.042"></circle><circle id="led-300" cx="740.69" cy="254.885"></circle><circle id="led-301" cx="756.199" cy="250.912"></circle><circle id="led-302" cx="765.219" cy="245.506"></circle><circle id="led-303" cx="761.706" cy="239.36"></circle><circle id="led-304" cx="748.492" cy="235.174"></circle><circle id="led-305" cx="732.124" cy="231.646"></circle><circle id="led-306" cx="719.157" cy="227.409"></circle><circle id="led-307" cx="716.108" cy="221.312"></circle><circle id="led-308" cx="725.224" cy="216.273"></circle><circle id="led-309" cx="740.815" cy="212.583"></circle><circle id="led-310" cx="756.324" cy="208.876"></circle><circle id="led-311" cx="765.194" cy="203.785"></circle><circle id="led-312" cx="761.827" cy="197.719"></circle><circle id="led-313" cx="748.695" cy="193.516"></circle><circle id="led-314" cx="732.326" cy="189.989"></circle><circle id="led-315" cx="719.274" cy="185.769"></circle><circle id="led-316" cx="716.062" cy="179.688"></circle><circle id="led-317" cx="725.052" cy="174.621"></circle><circle id="led-318" cx="740.601" cy="170.923"></circle><circle id="led-319" cx="756.152" cy="167.224"></circle><circle id="led-320" cx="765.15" cy="162.16"></circle><circle id="led-321" cx="761.946" cy="156.077"></circle><circle id="led-322" cx="748.9" cy="151.856"></circle><circle id="led-323" cx="732.531" cy="148.328"></circle><circle id="led-324" cx="719.394" cy="144.126"></circle><circle id="led-325" cx="716.017" cy="138.059"></circle><circle id="led-326" cx="724.879" cy="132.965"></circle><circle id="led-327" cx="740.384" cy="129.257"></circle><circle id="led-328" cx="755.977" cy="125.567"></circle><circle id="led-329" cx="765.102" cy="120.529"></circle><circle id="led-330" cx="762.066" cy="114.431"></circle><circle id="led-331" cx="749.107" cy="110.191"></circle><circle id="led-332" cx="732.739" cy="106.663"></circle><circle id="led-333" cx="719.518" cy="102.479"></circle><circle id="led-334" cx="715.974" cy="96.429"></circle><circle id="led-335" cx="724.705" cy="91.308"></circle><circle id="led-336" cx="740.165" cy="87.59"></circle><circle id="led-337" cx="755.8" cy="83.909"></circle><circle id="led-338" cx="765.052" cy="78.897"></circle><circle id="led-339" cx="762.184" cy="72.784"></circle><circle id="led-340" cx="749.314" cy="68.525"></circle><circle id="led-341" cx="732.949" cy="64.996"></circle><circle id="led-342" cx="719.644" cy="60.829"></circle><circle id="led-343" cx="715.949" cy="54.702"></circle><circle id="led-344" cx="724.855" cy="49.296"></circle><circle id="led-345" cx="740.353" cy="45.383"></circle><circle id="led-346" cx="755.064" cy="41.293"></circle><circle id="led-347" cx="761.607" cy="35.35"></circle></g></g></g></g></svg>');
