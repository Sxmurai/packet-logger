# packet-logger
My version of https://github.com/zzurio/Packet-Logger

---

## What's different about your version than zzurio's?

Mine is a bit different, but theirs was an inspiration for the project. It only logs the packet qualified name, not any of the data and whatnot.

So, I'm going to solve that. It will log:

- Packet Fully Qualified Name (basically the net.minecraft.network.etc shit)
- Packet fields (so for example if it logs CPacketPlayer it'll show if its rotating or moving etc)
- Time packet was sent

## What the fuck do I even use this for?

Well, you could always use it to figure out how someone did something in a client. For example, Pyro. Instead of attempting to dump and decompile fully (good luck) you can record packets and figure out how they did things. (simple things, of course!) For example, NCPStrict Criticals. If you know the player position when you attack someone you could figure out by how much they send a packet to bypass. For example: (this might not be what the log will look like

```
Logged: CPacketPlayer.Position (net.minecraft.network.play.client.CPacketPlayer$Position)
Fields:
  - x (125.5)
  - y (78.062602401692772)
  - z (5472.84)
  - yaw (67.0f)
  - pitch (100.0f)
  - onGround (true)
  - moving (true)
  - rotating (false)
```

So, we can see the y value (which for this example makes sense why we would want to look at this value, as for criticals you need to move upwards) and we can see it's a very specific number. We know that the `.062602401692772` has meaning, plus we know that our y coordinate is 78. So we can infer that they sent a movement packet going upwards `.062602401692772`.

While a shitty example, you can always figure it out.

## That seems super complicated! How would I know what to do as a beginner?

You kinda don't. It's a tool to observe what is being sent. If you know how to use it, you know how to use it.

---

<h5 align="center">sxmurai - 2021</h5>
