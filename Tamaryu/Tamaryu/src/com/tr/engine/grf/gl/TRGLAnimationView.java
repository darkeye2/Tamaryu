package com.tr.engine.grf.gl;

public class TRGLAnimationView extends TRGLImageView {

}


/*color to 8bit
 * 
 * <?php
$r = 0;
$g = 0;
$b = 0;

for($r = 0; $r <=255; $r+=5){
	for($g = 0; $g <=255; $g+=5){
		for($b = 0; $b <=255; $b+=5){
			//$color = ($r*6/256)*36 + ($g*6/256)*6 + ($b*6/256);
			$color = (floor(($r / 32)) << 5) + (floor(($g / 32)) << 2) + floor(($b / 64));
			$nr = ($color >> 5) * 32;
			$ng = (($color & 28) >> 2) * 32;
			$nb = ($color & 3) * 64;
			//$nr  = round(($color >> 5) * 255 / 7);
			//$ng = round((($color >> 2) & 0x07) * 255 / 7);
			//$nb  = round(($color & 0x03) * 255 / 3);
			print("<div style='width:100px; height:25px; display:inline-block; background-color:rgb(".$r.", ".$g.", ".$b.");'>(".$r.", ".$g.", ".$b.")</div>");
			print("<div style='width:100px; height:25px; display:inline-block; background-color:rgb(".$nr.", ".$ng.", ".$nb.");'>(".$nr.", ".$ng.", ".$nb.")</div><br>");
		}
	}
}


?>
 * 
 * 
 * */
