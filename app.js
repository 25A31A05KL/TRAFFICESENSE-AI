const canvas=document.getElementById('city'),ctx=canvas.getContext('2d');
let running=false, obstacle=false, tick=0, vehicles=[], signalGreen=true, weather='CLEAR';
const roads=[{x:0,y:225,w:900,h:90},{x:405,y:0,w:90,h:510}];
function seed(){vehicles=Array.from({length:8},(_,i)=>({id:i+1,x:70+i*95,y:i%2?270:250,dir:i%2?'h':'v',speed:.7+Math.random()*.8,type:'CAR',emergency:false}));}
seed();
function draw(){
 ctx.clearRect(0,0,900,510);ctx.fillStyle='#102b29';ctx.fillRect(0,0,900,510);
 ctx.fillStyle='#27364a';roads.forEach(r=>ctx.fillRect(r.x,r.y,r.w,r.h));
 ctx.strokeStyle='#8090a5';ctx.setLineDash([18,14]);ctx.lineWidth=2;ctx.beginPath();ctx.moveTo(0,270);ctx.lineTo(900,270);ctx.moveTo(450,0);ctx.lineTo(450,510);ctx.stroke();ctx.setLineDash([]);
 ctx.fillStyle='#263e36';ctx.fillRect(20,20,350,155);ctx.fillRect(535,350,340,130);
 ctx.fillStyle='#8ca4b8';ctx.font='13px Arial';ctx.fillText('NORTH DISTRICT',35,45);ctx.fillText('SOUTH PARK',550,375);
 // intersection signal indicators
 ctx.fillStyle=signalGreen?'#38df94':'#f45e6d';ctx.beginPath();ctx.arc(520,205,9,0,Math.PI*2);ctx.fill();
 ctx.fillStyle=signalGreen?'#f45e6d':'#38df94';ctx.beginPath();ctx.arc(380,330,9,0,Math.PI*2);ctx.fill();
 vehicles.forEach(v=>{
   ctx.save();ctx.translate(v.x,v.y);ctx.fillStyle=v.emergency?'#f15e68':v.type==='BUS'?'#e8b85d':v.type==='BIKE'?'#9d8cff':'#55c9e8';
   ctx.fillRect(-13,-7,26,14);ctx.fillStyle='#102033';ctx.fillRect(-7,-5,9,10);ctx.restore();
 });
 if(obstacle){ctx.fillStyle='#ff7c62';ctx.fillRect(610,255,24,24);ctx.fillStyle='white';ctx.fillText('OBSTACLE',640,270)}
 if(weather==='RAIN'){ctx.strokeStyle='#65a9ed';for(let i=0;i<35;i++){let x=(i*73+tick*4)%900,y=(i*41+tick*7)%510;ctx.beginPath();ctx.moveTo(x,y);ctx.lineTo(x-5,y+12);ctx.stroke()}}
 if(weather==='FOG'){ctx.fillStyle='rgba(210,220,230,.23)';ctx.fillRect(0,0,900,510)}
}
function step(){if(running){tick++;vehicles.forEach(v=>{if(v.dir==='h'){v.x+=v.speed;if(v.x>920)v.x=-20}else{v.y+=v.speed;if(v.y>530)v.y=-20}});if(tick%260===0){signalGreen=!signalGreen;log('Adaptive signal phase changed.')} }draw();requestAnimationFrame(step)}
requestAnimationFrame(step);
function log(msg){const e=document.getElementById('events'),p=document.createElement('p');p.textContent=new Date().toLocaleTimeString()+' · '+msg;e.prepend(p);while(e.children.length>12)e.lastChild.remove()}
async function api(path,opts={}){try{let r=await fetch(path,{headers:{'Content-Type':'application/json'},...opts});if(!r.ok)throw Error(await r.text());return await r.json()}catch(e){log('API notice: '+e.message);return null}}
function updateStats(){document.getElementById('vehicleCount').textContent=vehicles.length;let d=vehicles.length<8?'LOW':vehicles.length<16?'MEDIUM':'HIGH';document.getElementById('density').textContent=d;document.getElementById('greenTime').textContent=d==='HIGH'?45:d==='MEDIUM'?30:20;document.getElementById('weatherValue').textContent=weather;document.getElementById('signalA').textContent=signalGreen?'GREEN':'RED';document.getElementById('signalA').className=signalGreen?'green':'red';document.getElementById('signalB').textContent=signalGreen?'RED':'GREEN';document.getElementById('signalB').className=signalGreen?'red':'green';}
document.getElementById('startBtn').onclick=()=>{running=true;log('Simulation started.');updateStats()};
document.getElementById('pauseBtn').onclick=()=>{running=false;log('Simulation paused.')};
document.getElementById('resetBtn').onclick=()=>{running=false;obstacle=false;weather='CLEAR';seed();updateStats();log('Simulation reset.')};
document.getElementById('addVehicle').onclick=async()=>{let type=document.getElementById('vehicleType').value;let v=await api('/api/vehicles',{method:'POST',body:JSON.stringify({type,emergency:false})});vehicles.push({id:v?.id||Date.now(),x:10,y:250+Math.random()*35,dir:'h',speed:1,type,emergency:false});updateStats();log(type+' added to virtual fleet.')};
document.getElementById('addAmbulance').onclick=async()=>{let v=await api('/api/vehicles',{method:'POST',body:JSON.stringify({type:'AMBULANCE',emergency:true})});vehicles.push({id:v?.id||Date.now(),x:10,y:290,dir:'h',speed:1.5,type:'AMBULANCE',emergency:true});updateStats();log('Emergency vehicle added. Simulated priority requested.')};
document.getElementById('applyWeather').onclick=async()=>{weather=document.getElementById('weather').value;await api('/api/weather',{method:'POST',body:JSON.stringify({condition:weather})});updateStats();log('Weather changed to '+weather+'.')};
document.getElementById('blockRoad').onclick=()=>{obstacle=!obstacle;log(obstacle?'Virtual obstacle introduced.':'Virtual obstacle removed.')};
document.getElementById('accident').onclick=async()=>{await api('/api/accidents',{method:'POST',body:JSON.stringify({roadId:'A-B',description:'User-triggered demo incident'})});log('Demo incident reported to backend.')};
document.getElementById('routeBtn').onclick=async()=>{let from=document.getElementById('from').value,to=document.getElementById('to').value;let r=await api('/api/routes/optimize',{method:'POST',body:JSON.stringify({from,to})});document.getElementById('routeResult').textContent=r?`Route: ${r.path.join(' → ')} | Distance cost: ${r.distance} | ${r.algorithm}`:'Could not calculate route.'};
async function loadParking(){let p=await api('/api/parking');if(!p)return;document.getElementById('parking').innerHTML='';Object.entries(p).forEach(([id,free])=>{let s=document.createElement('span');s.className='slot'+(free?'':' busy');s.textContent=id+' '+(free?'AVAILABLE':'OCCUPIED');document.getElementById('parking').append(s)})}
async function refresh(){let l=await api('/api/traffic/live');if(l){document.getElementById('vehicleCount').textContent=l.vehicleCount;document.getElementById('density').textContent=l.density;document.getElementById('greenTime').textContent=l.recommendedGreenSeconds;document.getElementById('weatherValue').textContent=l.weather}let f=await api('/api/fleet');if(f)document.getElementById('fleetSummary').textContent='Registered backend vehicles: '+f.total;loadParking()}
setInterval(refresh,5000);refresh();updateStats();log('TrafficSense AI frontend initialized.');
